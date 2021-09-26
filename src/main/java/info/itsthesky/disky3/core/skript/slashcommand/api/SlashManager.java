package info.itsthesky.disky3.core.skript.slashcommand.api;

import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.interactions.commands.Command;

public final class SlashManager {

    public static void unregister(SlashObject command) {

        for (Bot bot : BotManager.getLoadedBots()) {

            bot.getCore().retrieveCommands().queue(cmds -> {

                for (Command command1 : cmds) {
                    if (!command1.getName().equalsIgnoreCase(command.getName()))
                        continue;
                    command1.delete().queue();
                }

            });

        }
    }
}
