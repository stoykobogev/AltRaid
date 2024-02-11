package altraid;

import altraid.handlers.button.ButtonHandler;
import altraid.handlers.command.CommandHandler;
import altraid.handlers.modal.ModalHandler;
import altraid.handlers.selectmenu.SelectMenuHandler;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static altraid.Constants.EMOJIS;
import static altraid.Singletons.*;

public class App {

  private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
  private static final String BOT_TOKEN = "MTE5NjU2NjQ5NDQ0NDc4NTc3NQ.Gb9uIl.yh17Aoz_j7lVgBOKNID7cej2dIv7INfbaKSzNY";

  public static void main(String[] args) {
    try {
      run();
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    } finally {
      USER_CACHE_MANAGER.saveCache();
      ROLE_CACHE_MANAGER.saveCache();
    }
  }

  private static void run() {
    CLIENT = DiscordClient.create(BOT_TOKEN);
    GatewayDiscordClient gateway = CLIENT.login().block();

    commandRegistrar(CLIENT);

    gateway.getGuildEmojis(Snowflake.of("1197265218561126410")).subscribe(EMOJIS::add);

    gateway.on(ChatInputInteractionEvent.class, CommandHandler::handle)
        .doOnError(Util::errorHandler)
        .subscribe();

    gateway.on(ButtonInteractionEvent.class, ButtonHandler::handle)
        .doOnError(Util::errorHandler)
        .subscribe();

    gateway.on(ModalSubmitInteractionEvent.class, ModalHandler::handle)
    .doOnError(Util::errorHandler)
    .subscribe();

    gateway.on(SelectMenuInteractionEvent.class, SelectMenuHandler::handle)
        .doOnError(Util::errorHandler)
        .subscribe();

    gateway.onDisconnect().block();
  }

  private static void commandRegistrar(DiscordClient client) {
    List<String> commands = List.of("altraid.json", "altraidRoles.json");
    try {
      new GlobalCommandRegistrar(client).registerCommands(commands);
    } catch (Exception e) {
      LOGGER.error("Error trying to register global slash commands", e);
    }
  }
}
