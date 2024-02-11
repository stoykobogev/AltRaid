package altraid.handlers.selectmenu;

import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;

import java.util.List;

import static altraid.Constants.ALL_CHARACTERS;
import static altraid.Singletons.USER_CACHE_MANAGER;
import static altraid.handlers.HandlerUtil.*;

public class SignOutSelectMenuHandler implements Handler {

  @Override
  public void handle(SelectMenuInteractionEvent event) {
    String customId = event.getCustomId();
    List<String> selectedOptions = event.getValues();
    long memberId = event.getInteraction().getMember().get().getId().asLong();
    long messageId = extractMessageId(customId);
    replyDismiss(event);

    USER_CACHE_MANAGER.useCache(cache -> {
      if (selectedOptions.contains(ALL_CHARACTERS)) {
        cache.removeIf(userData ->
            userData.mainMessageId == messageId && userData.memberId == memberId);
      } else {
        cache.removeIf(userData ->
            userData.mainMessageId == messageId
                && userData.memberId == memberId
                && selectedOptions.contains(userData.characterName)
        );
      }

      USER_CACHE_MANAGER.saveCache();
      updateMessage(event.getInteraction().getChannelId().asLong(), messageId);
    });
  }
}
