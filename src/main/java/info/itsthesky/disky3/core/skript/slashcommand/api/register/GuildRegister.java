package info.itsthesky.disky3.core.skript.slashcommand.api.register;

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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author ItsTheSky
 */
public final class GuildRegister extends RegisterDB {

    private static final GuildRegister instance;

    static {
        instance = new GuildRegister();
    }

    public GuildRegister() {
        super();
    }

    public static GuildRegister getInstance() {
        return instance;
    }

    public void registerCommand(List<String> guilds, SlashObject command) {
        command.setGlobalRegister(true);
        Bukkit.getScheduler().runTaskLater(
                DiSky.getInstance(),
                () -> registerCommand(command, SlashUtils.parseGuilds(guilds)),
                10
        );
    }

    public void registerCommand(SlashObject command, List<Guild> guilds) {
        CommandData data = SlashUtils.parseCommand(command);
        if (data == null) return;

        for (Guild guild : guilds) {
            AtomicBoolean shouldContinue = new AtomicBoolean(true);
            guild.retrieveCommands().queue(cmds -> {
                for (Command loadedCommand : cmds) {
                    if (!loadedCommand.getName().equalsIgnoreCase(data.getName()))
                        return;
                    // Mean they are equal, so we can just edit them
                    if (alreadyRegistered(BotManager.searchFromJDA(guild.getJDA()), data)) {

                        shouldContinue.set(false);
                        DiSky.debug("Registering command " + command.getName() + " using edit way.");
                        editCommand(data, loadedCommand).queue();
                        return;

                    }
                }
                // We have to create them

                if (!shouldContinue.get())
                    return;
                DiSky.debug("Registering command " + command.getName() + " using creation way.");
                createCommand(guild, data).queue();
            });
        }
    }

    public void unregister(List<String> guilds, SlashObject cmd) {
        for (Guild guild : SlashUtils.parseGuilds(guilds)) {
            guild.retrieveCommands().queue(
                    cmds -> cmds.forEach(cmd2 -> {
                        if (cmd2.getName().equalsIgnoreCase(cmd.getName()))
                            cmd2.delete().queue();
                    })
            );
        }
    }

    public CommandListUpdateAction createCommand(Guild guild, CommandData data) {
        Bot bot = BotManager.searchFromJDA(guild.getJDA());
        addCommand(bot, data, true);
        return guild
                .updateCommands()
                .addCommands(getCommands(bot).values());
    }
}
