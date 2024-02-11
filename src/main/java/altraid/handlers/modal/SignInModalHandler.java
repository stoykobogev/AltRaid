package altraid.handlers.modal;

import altraid.UserData;
import altraid.Util;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;

import static altraid.Constants.*;
import static altraid.Singletons.USER_CACHE_MANAGER;
import static altraid.handlers.HandlerUtil.extractMessageId;
import static altraid.handlers.HandlerUtil.getUserData;

public class SignInModalHandler implements Handler {

  @Override
  public void handle(ModalSubmitInteractionEvent event) {
    long messageId = extractMessageId(event.getCustomId());
    UserData userData = getUserData(event);
    String characterName = event.getComponents().get(0).getData().components().get().get(0).value().get();

    boolean characterNameExists = USER_CACHE_MANAGER.useCache(cache -> {
      return cache.stream()
          .anyMatch(u -> u.mainMessageId == messageId && characterName.equals(u.characterName));
    });

    if (characterNameExists) {
      event.reply(InteractionApplicationCommandCallbackSpec.builder()
              .ephemeral(true)
              .addEmbed(EmbedCreateSpec.builder()
                  .title("Character name already exists")
                  .build())
              .build())
          .doOnError(Util::errorHandler)
          .block();
    } else {
      userData.characterName = characterName;

      event.reply(InteractionApplicationCommandCallbackSpec.builder()
              .ephemeral(true)
              .addEmbed(EmbedCreateSpec.builder()
                  .title("Pick a role")
                  .build())
              .addComponent(ActionRow.of(
                  SelectMenu.of(SIGN_IN_SUBMIT_2,
                      SelectMenu.Option.of("Tank", TANK_ROLE),
                      SelectMenu.Option.of("Healer", HEALER_ROLE),
                      SelectMenu.Option.of("Damage dealer", DAMAGE_DEALER_ROLE)
                  )))
              .build())
          .doOnError(Util::errorHandler)
          .block();

      userData.messageIds.add(event.getReply().block().getId().asLong());
    }
  }
}
