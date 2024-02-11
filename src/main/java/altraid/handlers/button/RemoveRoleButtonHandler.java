package altraid.handlers.button;

import altraid.Util;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;

import static altraid.Constants.REMOVE_ROLE_SUBMIT_2;

public class RemoveRoleButtonHandler implements Handler {

  @Override
  public void handle(ButtonInteractionEvent event, Message message, long memberId) {
    event.edit()
        .withEmbeds(EmbedCreateSpec.builder()
            .title("Pick role to remove")
            .build())
        .withComponents(ActionRow.of(
            SelectMenu.ofRole(REMOVE_ROLE_SUBMIT_2)
        ))
        .doOnError(Util::errorHandler)
        .block();
  }
}
