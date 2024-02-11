package altraid.handlers.selectmenu;

import altraid.UserData;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;

import static altraid.handlers.HandlerUtil.*;

public class SignIn3SelectMenuHandler implements Handler {

  @Override
  public void handle(SelectMenuInteractionEvent event) {
    String characterClass = event.getValues().get(0);
    UserData userData = getUserData(event);
    userData.characterClass = characterClass;

    replyDismiss(event);

    updateMessageAfterSignIn(userData);
  }
}
