package info.itsthesky.disky3.core.skript.slashcommand.api.register;

import ch.njol.util.Pair;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.core.skript.slashcommand.api.SlashObject;
import info.itsthesky.disky3.core.skript.slashcommand.api.SlashUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.bukkit.Bukkit;

import java.util.List;

/**
 * @author ItsTheSky
 */
public final class GuildRegister extends RegisterDB {

    public static void registerCommand(List<String> guilds, SlashObject command) {
        Bukkit.getScheduler().runTaskLater(
                DiSky.getInstance(),
                () -> registerCommand(command, SlashUtils.parseGuilds(guilds)),
                30
        );
    }

    public static void registerCommand(SlashObject command, List<Guild> guilds) {
        CommandData data = SlashUtils.parseCommand(command);
        if (data == null) return;

        for (Guild guild : guilds) {
            guild.retrieveCommands().queue(cmds -> {
                for (Command loadedCommand : cmds) {
                    if (!loadedCommand.getName().equalsIgnoreCase(data.getName()))
                        continue;
                    // Mean they are equal, so we can just edit them
                    if (!SlashUtils.compareCommand(loadedCommand, data)) {

                        DiSky.debug("Registering command " + command.getName() + " using edit way.");
                        editCommand(data, loadedCommand).queue();
                        return;

                    }
                }
                // We have to create them

                DiSky.debug("Registering command " + command.getName() + " using creation way.");
                createCommand(guild, data).queue();
            });
        }
    }

    public static CommandListUpdateAction createCommand(Guild guild, CommandData data) {
        Bot bot = BotManager.searchFromJDA(guild.getJDA());
        addCommand(bot, data);
        return guild
                .updateCommands()
                .addCommands(getCommands(bot));
    }
}
