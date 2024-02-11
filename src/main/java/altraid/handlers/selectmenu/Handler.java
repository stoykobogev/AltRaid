package altraid.handlers.selectmenu;

import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;

public interface Handler {

  void handle(SelectMenuInteractionEvent event);
}
