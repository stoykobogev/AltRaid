package altraid.handlers.modal;

import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

import static altraid.Constants.*;

public class ModalHandler {

  private static final Set<Map.Entry<String, Handler>> SET = Map.of(
      SIGN_IN_SUBMIT_1, (Handler) new SignInModalHandler()
  ).entrySet();

  public static Mono<Void> handle(ModalSubmitInteractionEvent event) {
    for (Map.Entry<String, Handler> entry : SET) {
      if (event.getCustomId().startsWith(entry.getKey())) {
        entry.getValue().handle(event);
        break;
      }
    }

    return Mono.empty();
  }
}
