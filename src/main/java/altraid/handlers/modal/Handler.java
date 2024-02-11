package altraid.handlers.modal;

import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;

public interface Handler {

  void handle(ModalSubmitInteractionEvent event);
}
