package altraid.handlers.button;

import altraid.Util;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;

import static altraid.Constants.DIST_ID;
import static altraid.Constants.RIHANA_ID;
import static altraid.handlers.HandlerUtil.*;

public class CloseButtonHandler implements Handler {

  @Override
  public void handle(ButtonInteractionEvent e, Message message, long memberId) {
    //            if (hasRequiredRole(event, client)) {
    if (RIHANA_ID == memberId || DIST_ID == memberId) {
      message.edit()
          .withEmbeds(buildDefaultEmbedSpec(message.getEmbeds().get(0).getTitle().get(), Color.RED))
          .withComponents(
              ActionRow.of(
                  SelectMenu.of("selectMenu0",
                          SelectMenu.Option.of("This event is closed.", "This event is closed.")
                              .withDefault(true))
                      .disabled()))
          .doOnError(Util::errorHandler)
          .block();

      deleteMessageUsers(message.getId().asLong());
    } else {
      replyNoPermission(e);
    }
  }
}
