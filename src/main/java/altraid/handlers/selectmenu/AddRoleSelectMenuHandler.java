package altraid.handlers.selectmenu;

import altraid.Util;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;

import java.util.HashSet;

import static altraid.Singletons.ROLE_CACHE_MANAGER;

public class AddRoleSelectMenuHandler implements Handler {

  @Override
  public void handle(SelectMenuInteractionEvent event) {
    String roleId = event.getValues().get(0);
    long guildId = event.getInteraction().getGuildId().get().asLong();
    ROLE_CACHE_MANAGER.useCache(cache -> {
      cache.computeIfAbsent(guildId, k -> new HashSet<>()).add(roleId);
      ROLE_CACHE_MANAGER.saveCache();
    });

    event.edit()
        .withEmbeds(EmbedCreateSpec.builder()
            .title("Role added")
            .build())
        .withComponents()
        .doOnError(Util::errorHandler)
        .block();
  }
}
