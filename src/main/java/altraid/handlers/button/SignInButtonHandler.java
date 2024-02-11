package altraid.handlers.button;

import altraid.UserData;
import altraid.Util;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.TextInput;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.InteractionPresentModalSpec;

import static altraid.Constants.SIGN_IN_SUBMIT_1;
import static altraid.Singletons.USER_CACHE_MANAGER;

public class SignInButtonHandler implements Handler {

  @Override
  public void handle(ButtonInteractionEvent event, Message message, long memberId) {
    Member member = event.getInteraction().getMember().get();
    long messageId = event.getMessageId().asLong();

    UserData userData = new UserData();
    userData.messageIds.add(messageId);
    userData.mainMessageId = messageId;
    userData.channelId = message.getChannelId().asLong();
    userData.guildName = member.getDisplayName();
    userData.memberId = member.getId().asLong();

    USER_CACHE_MANAGER.useCache(cache -> {
      cache.add(userData);
      USER_CACHE_MANAGER.saveCache();
    });

    event.presentModal(InteractionPresentModalSpec.builder()
            .customId(SIGN_IN_SUBMIT_1 + "_" + messageId)
            .title("Sign in to raid")
            .addComponent(ActionRow.of(
                TextInput.small("character-name", "Character name").required(true)))
            .build())
        .doOnError(Util::errorHandler)
        .block();
  }
}
