package altraid.handlers.command;

import altraid.Util;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.discordjson.json.RoleDataFields;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

import static altraid.Constants.ADD_ROLE_SUBMIT_1;
import static altraid.Constants.REMOVE_ROLE_SUBMIT_1;
import static altraid.Singletons.CLIENT;
import static altraid.Singletons.ROLE_CACHE_MANAGER;
import static altraid.handlers.HandlerUtil.*;

public class CommandHandler {

  public static Publisher<Mono<Void>> handle(ChatInputInteractionEvent event) {
    String command = event.getCommandName();

    if (command.equals("altraid")) {
      if (hasRequiredRole(event, CLIENT)) {
        String raid = event.getOptions().get(0).getValue().get().asString();
        int date = (int) event.getOptions().get(1).getValue().get().asLong();
        int month = (int) event.getOptions().get(2).getValue().get().asLong();
        String time = event.getOptions().get(3).getValue().get().asString();
        LocalDate messageDate = LocalDate.of(LocalDate.now().getYear(), month, date);

        event.reply(buildDefaultMessageSpec(buildTitle(raid, messageDate, time)))
            .doOnError(Util::errorHandler)
            .block();
      } else {
        replyNoPermission(event);
      }
    } else if (command.equals("altraidroles")) {
      long guildId = event.getInteraction().getGuildId().get().asLong();
      String roles = CLIENT.getGuildService().getGuildRoles(guildId)
          .collectList().block()
          .stream()
          .filter(r -> Optional.ofNullable(ROLE_CACHE_MANAGER.useCache(cache -> {
                return cache.get(guildId);
              }))
              .map(s -> s.contains(r.id().asString()))
              .orElse(false))
          .map(RoleDataFields::name)
          .collect(Collectors.joining(", "));

      event.reply(InteractionApplicationCommandCallbackSpec.builder()
              .ephemeral(true)
              .addEmbed(EmbedCreateSpec.builder()
                  .title("Add or Remove role")
                  .addField(EmbedCreateFields.Field.of("Current Roles:", roles, false))
                  .build())
              .addComponent(ActionRow.of(
                  Button.primary(ADD_ROLE_SUBMIT_1, "Add"),
                  Button.primary(REMOVE_ROLE_SUBMIT_1, "Remove")
              ))
              .build())
          .doOnError(Util::errorHandler)
          .block();
    }

    return Mono.empty();
  }
}
