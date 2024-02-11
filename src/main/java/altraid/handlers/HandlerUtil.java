package altraid.handlers;

import altraid.UserData;
import altraid.Util;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.interaction.ComponentInteractionEvent;
import discord4j.core.event.domain.interaction.DeferrableInteractionEvent;
import discord4j.core.event.domain.interaction.InteractionCreateEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.component.LayoutComponent;
import discord4j.core.object.entity.GuildEmoji;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.discordjson.json.*;
import discord4j.rest.entity.RestMessage;
import discord4j.rest.util.Color;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static altraid.Constants.*;
import static altraid.Singletons.*;

public class HandlerUtil {

  public static void replyNoPermission(DeferrableInteractionEvent event) {
    event.reply(InteractionApplicationCommandCallbackSpec.builder()
            .ephemeral(true)
            .addEmbed(EmbedCreateSpec.builder()
                .title("You don't have permission")
                .build())
            .build())
        .doOnError(Util::errorHandler)
        .block();
  }

  public static boolean hasRequiredRole(InteractionCreateEvent event, DiscordClient client) {
    Member member = event.getInteraction().getMember().get();
    long guildId = event.getInteraction().getGuildId().get().asLong();

    if (member.getRoleIds().stream()
        .anyMatch(id -> Optional.ofNullable(ROLE_CACHE_MANAGER.useCache(cache -> {
          return cache.get(guildId);
        }))
        .map(s -> s.contains(id.asString()))
        .orElse(false))) {
      return true;
    } else {
      String ownerId = client.getGuildService().getGuild(guildId).block()
          .ownerId().asString();

      return ownerId.equals(member.getId().asString());
    }
  }

  public static InteractionApplicationCommandCallbackSpec buildDefaultMessageSpec(String title) {
    return InteractionApplicationCommandCallbackSpec.builder()
        .addEmbed(buildDefaultEmbedSpec(title, Color.BLUE))
        .addAllComponents(buildActionRows())
        .build();
  }

  public static EmbedCreateSpec buildDefaultEmbedSpec(String title, Color color) {
    return EmbedCreateSpec.builder()
        .title(title)
        .color(color)
        .addField(EmbedCreateFields.Field.of("__Tanks__ (0)", "", true))
        .addField(EmbedCreateFields.Field.of("__Healers__ (0)", "", true))
        .addField(EmbedCreateFields.Field.of("__Damage Dealers__ (0)", "", true))
        .build();
  }

  public static List<LayoutComponent> buildActionRows() {
    return List.of(
        ActionRow.of(
            Button.success(SIGN_IN_ID, "Sign in"),
            Button.secondary(SIGN_OUT_1, "Sign out"),
            Button.primary(ABSENT_ID, "Absent")),
        ActionRow.of(
            Button.primary(CLOSE_ID, "Close event"),
            Button.danger(DELETE_ID, "Delete event"))
    );
  }

  public static String buildTitle(String raid, LocalDate localDate, String time) {
    return String.format("Raid: __%s__%sDate: __%s.%s.__%sServer time: __%s__",
        raid, System.lineSeparator(), getDate(localDate.getDayOfMonth()),
        getDate(localDate.getMonth().getValue()), System.lineSeparator(), time);
  }

  public static void deleteMessageUsers(long messageId) {
    USER_CACHE_MANAGER.useCache(cache -> {
      cache.removeIf(d -> d.mainMessageId == messageId);
      USER_CACHE_MANAGER.saveCache();
    });
  }

  public static UserData getUserData(ComponentInteractionEvent event) {
    long memberId = event.getInteraction().getMember().get().getId().asLong();

    List<UserData> userDataList = USER_CACHE_MANAGER.useCache(cache -> {
      return cache.stream()
          .filter(d -> d.messageIds.contains(event.getMessageId().asLong()) && d.memberId == memberId)
          .collect(Collectors.toList());
    });

    return userDataList.get(userDataList.size() - 1);
  }

  private static String getDate(int date) {
    return date < 9
        ? "0" + date
        : String.valueOf(date);
  }

  public static void updateMessage(long channelId, long mainMessageId) {
    USER_CACHE_MANAGER.useCache(cache -> {
      List<UserData> tanks = new ArrayList<>();
      List<UserData> healers = new ArrayList<>();
      List<UserData> damageDealers = new ArrayList<>();
      List<UserData> absences = new ArrayList<>();

      for (UserData d : cache) {
        if (d.characterClass == null && !d.absent) {
          continue;
        }

        if (d.mainMessageId == mainMessageId) {
          if (TANK_ROLE.equals(d.role)) {
            tanks.add(d);
          } else if (HEALER_ROLE.equals(d.role)) {
            healers.add(d);
          } else if (DAMAGE_DEALER_ROLE.equals(d.role)) {
            damageDealers.add(d);
          } else if (d.absent) {
            absences.add(d);
          }
        }
      }

      USER_CACHE_MANAGER.saveCache();

      updateMessage(channelId, mainMessageId, tanks, healers, damageDealers, absences, cache);
    });
  }

  public static void updateMessageAfterSignIn(UserData userData) {
    USER_CACHE_MANAGER.useCache(cache -> {
      List<UserData> tanks = new ArrayList<>();
      List<UserData> healers = new ArrayList<>();
      List<UserData> damageDealers = new ArrayList<>();
      List<UserData> absences = new ArrayList<>();
      Iterator<UserData> iterator = cache.iterator();

      while (iterator.hasNext()) {
        UserData d = iterator.next();

        if (d.characterClass == null) {
          if (userData.memberId == d.memberId) {
            iterator.remove();
          }
          continue;
        }

        if (d.mainMessageId == userData.mainMessageId) {
          if (TANK_ROLE.equals(d.role)) {
            tanks.add(d);
          } else if (HEALER_ROLE.equals(d.role)) {
            healers.add(d);
          } else if (DAMAGE_DEALER_ROLE.equals(d.role)) {
            damageDealers.add(d);
          } else if (d.absent) {
            absences.add(d);
          }
        }
      }

      USER_CACHE_MANAGER.saveCache();

      updateMessage(userData.channelId, userData.mainMessageId, tanks, healers, damageDealers, absences, cache);
    });
  }

  public static void updateMessage(long channelId, long mainMessageId, List<UserData> tanks,
                                   List<UserData> healers, List<UserData> damageDealers, List<UserData> absences,
                                   List<UserData> allUsers) {
    RestMessage message = CLIENT.getMessageById(Snowflake.of(channelId), Snowflake.of(mainMessageId));
    EmbedData embed = message.getData().block().embeds().get(0);
    ImmutableEmbedData.Builder embedDataBuilder = EmbedData.builder()
        .title(embed.title())
        .color(embed.color())
        .addField(EmbedFieldData.builder()
            .name(String.format("__Tanks__ (%s)", tanks.size()))
            .value(convertUserDataToString(tanks, allUsers))
            .inline(true)
            .build())
        .addField(EmbedFieldData.builder()
            .name(String.format("__Healers__ (%s)", healers.size()))
            .value(convertUserDataToString(healers, allUsers))
            .inline(true)
            .build())
        .addField(EmbedFieldData.builder()
            .name(String.format("__Damage Dealers__ (%s)", damageDealers.size()))
            .value(convertUserDataToString(damageDealers, allUsers))
            .inline(true)
            .build());

    if (!absences.isEmpty()) {
      embedDataBuilder.addField(EmbedFieldData.builder()
          .name(String.format("__Absent__ (%s)", absences.size()))
          .value(absences.stream()
              .map(a -> a.guildName)
              .collect(Collectors.joining(System.lineSeparator())))
          .inline(true)
          .build());
    }

    message.edit(MessageEditRequest.builder()
            .embeds(List.of(embedDataBuilder.build()))
            .build())
        .block();
  }

  private static String convertUserDataToString(List<UserData> userDataList, List<UserData> allUsers) {
    return userDataList.stream()
        .map(d -> {
          GuildEmoji emoji = findEmoji(d.characterClass);

          boolean hasMultipleAltsInEvent = allUsers.stream()
              .filter(d2 -> d2.memberId == d.memberId && d2.mainMessageId == d.mainMessageId)
              .count() > 1;

          if (hasMultipleAltsInEvent) {
            return String.format("<:%s:%s> %s (%s)", emoji.getName(), emoji.getId().asString(), d.characterName,
                d.guildName);
          } else {
            return String.format("<:%s:%s> %s", emoji.getName(), emoji.getId().asString(), d.characterName);
          }
        })
        .collect(Collectors.joining(System.lineSeparator()));
  }

  private static GuildEmoji findEmoji(String characterClass) {
    characterClass = characterClass.toLowerCase();

    for (GuildEmoji emoji : EMOJIS) {
      if (emoji.getName().contains(characterClass)) {
        return emoji;
      }
    }

    return null;
  }

  public static void replyDismiss(ComponentInteractionEvent event) {
    event.edit(InteractionApplicationCommandCallbackSpec.builder()
            .ephemeral(true)
            .embeds(new ArrayList<>())
            .components(new ArrayList<>())
            .content("You can dismiss this message")
            .build()).doOnError(Util::errorHandler)
        .block();
  }

  public static long extractMessageId(String customId) {
    return Long.parseLong(customId.substring(customId.lastIndexOf("_") + 1));
  }
}
