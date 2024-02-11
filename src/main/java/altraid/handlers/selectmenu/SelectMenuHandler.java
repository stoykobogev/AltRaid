package altraid.handlers.selectmenu;

import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

import static altraid.Constants.*;

public class SelectMenuHandler {

  private static final Set<Map.Entry<String, Handler>> set = Map.of(
      SIGN_IN_SUBMIT_2, new SignIn2SelectMenuHandler(),
      SIGN_IN_SUBMIT_3, new SignIn3SelectMenuHandler(),
      ADD_ROLE_SUBMIT_2, new AddRoleSelectMenuHandler(),
      REMOVE_ROLE_SUBMIT_2, new RemoveRoleSelectMenuHandler(),
      SIGN_OUT_2, new SignOutSelectMenuHandler()
  ).entrySet();

  public static Mono<Void> handle(SelectMenuInteractionEvent event) {
    for (Map.Entry<String, Handler> entry : set) {
      if (event.getCustomId().startsWith(entry.getKey())) {
        entry.getValue().handle(event);
        break;
      }
    }

    return Mono.empty();
  }
}
