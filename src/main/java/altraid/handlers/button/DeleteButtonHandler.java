package altraid.handlers.button;

import altraid.Util;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.entity.Message;

import static altraid.Constants.DIST_ID;
import static altraid.Constants.RIHANA_ID;
import static altraid.Singletons.CLIENT;
import static altraid.handlers.HandlerUtil.*;

public class DeleteButtonHandler implements Handler {

  @Override
  public void handle(ButtonInteractionEvent e, Message message, long memberId) {
    if (hasRequiredRole(e, CLIENT)) {
      if (RIHANA_ID == memberId || DIST_ID == memberId) {
        message.delete()
            .doOnError(Util::errorHandler)
            .block();

        deleteMessageUsers(message.getId().asLong());
      } else {
        replyNoPermission(e);
      }
    }
  }
}
