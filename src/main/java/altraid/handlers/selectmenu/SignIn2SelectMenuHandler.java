package altraid.handlers.selectmenu;

import altraid.Util;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;

import java.util.stream.Collectors;

import static altraid.Constants.ROLE_CLASS_MAP;
import static altraid.Constants.SIGN_IN_SUBMIT_3;
import static altraid.handlers.HandlerUtil.getUserData;

public class SignIn2SelectMenuHandler implements Handler {

  @Override
  public void handle(SelectMenuInteractionEvent event) {
    String role = event.getValues().get(0);
    getUserData(event).role = role;

    event.edit(InteractionApplicationCommandCallbackSpec.builder()
            .ephemeral(true)
            .addEmbed(EmbedCreateSpec.builder()
                .title("Pick a class")
                .build())
            .addComponent(ActionRow.of(
                SelectMenu.of(SIGN_IN_SUBMIT_3, ROLE_CLASS_MAP.get(role).stream()
                    .map(c -> SelectMenu.Option.of(c, c))
                    .collect(Collectors.toList()))))
            .build())
        .doOnError(Util::errorHandler)
        .block();
  }
}
