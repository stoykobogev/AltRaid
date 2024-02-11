package altraid.handlers.button;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public interface Handler {

  void handle(ButtonInteractionEvent event, Message message, long memberId);
}
