package altraid.handlers.button;

import altraid.Util;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static altraid.Constants.*;
import static altraid.Singletons.USER_CACHE_MANAGER;

public class SignOutButtonHandler implements Handler {

  @Override
  public void handle(ButtonInteractionEvent e, Message message, long memberId) {
    long messageId = e.getMessageId().asLong();
    List<String> characterNames = new ArrayList<>();
    characterNames.add(ALL_CHARACTERS);
    characterNames.addAll(USER_CACHE_MANAGER.useCache(cache -> {
      return cache.stream()
          .filter(d -> d.mainMessageId == messageId && d.memberId == memberId)
          .map(d -> d.characterName)
          .collect(Collectors.toList());
    }));

    e.reply(InteractionApplicationCommandCallbackSpec.builder()
            .ephemeral(true)
            .addEmbed(EmbedCreateSpec.builder()
                .title("Select characters")
                .build())
            .addComponent(ActionRow.of(
                SelectMenu.of(SIGN_OUT_2 + "_" + messageId,
                        characterNames.stream()
                            .map(cn -> SelectMenu.Option.of(cn, cn))
                            .collect(Collectors.toList()))
                    .withMinValues(1)
                    .withMaxValues(characterNames.size())
            ))
            .build())
        .doOnError(Util::errorHandler)
        .block();
  }
}
