package altraid.handlers.button;

import altraid.UserData;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;

import static altraid.Singletons.USER_CACHE_MANAGER;
import static altraid.handlers.HandlerUtil.updateMessage;

public class AbsentButtonHandler implements Handler {

  @Override
  public void handle(ButtonInteractionEvent event, Message message, long memberId) {
    long messageId = message.getId().asLong();
    long channelId = event.getMessage().get().getChannelId().asLong();
    Member member = event.getInteraction().getMember().get();

    event.deferEdit().subscribe();

    USER_CACHE_MANAGER.useCache(cache -> {
      cache.removeIf(u -> u.memberId == memberId && u.mainMessageId == messageId);

      UserData userData = new UserData();
      userData.messageIds.add(messageId);
      userData.mainMessageId = messageId;
      userData.channelId = channelId;
      userData.guildName = member.getDisplayName();
      userData.memberId = member.getId().asLong();
      userData.absent = true;
      cache.add(userData);

      updateMessage(channelId, messageId);
    });
  }
}
