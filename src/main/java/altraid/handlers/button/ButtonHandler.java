package altraid.handlers.button;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

import static altraid.Constants.*;

public class ButtonHandler {

  private static final Map<String, Handler> MAP = Map.of(
      DELETE_ID, new DeleteButtonHandler(),
      CLOSE_ID, new CloseButtonHandler(),
      SIGN_OUT_1, new SignOutButtonHandler(),
      SIGN_IN_ID, new SignInButtonHandler(),
      ADD_ROLE_SUBMIT_1, new AddRoleButtonHandler(),
      REMOVE_ROLE_SUBMIT_1, new RemoveRoleButtonHandler(),
      ABSENT_ID, new AbsentButtonHandler()
  );

  public static Mono<Void> handle(ButtonInteractionEvent event) {
    Message message = event.getMessage().get();
    long memberId = event.getInteraction().getMember().get().getId().asLong();

    Optional.ofNullable(MAP.get(event.getCustomId()))
        .ifPresent(handler -> handler.handle(event, message, memberId));

    return Mono.empty();
  }
}
