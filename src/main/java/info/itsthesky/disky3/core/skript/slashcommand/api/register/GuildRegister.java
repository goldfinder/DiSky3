package info.itsthesky.disky3.core.skript.slashcommand.api.register;

import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.core.skript.slashcommand.api.SlashObject;
import info.itsthesky.disky3.core.skript.slashcommand.api.SlashUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.BaseCommand;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author ItsTheSky
 */
public final class GuildRegister extends RegisterDB {

    private static final GuildRegister instance;

    static {
        instance = new GuildRegister();
    }

    public GuildRegister() {
        super("guilds");
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
        CommandData cmd = SlashUtils.parseCommand(command);
        if (cmd == null) return;

        for (Guild guild : guilds) {
            final Bot bot = BotManager.searchFromJDA(guild.getJDA());

            DiSky.debug("Getting guilds & registered commands ...");
            final List<CommandData> registeredCommands = Utils.convert(getCommands(bot).values());
            final List<Command> guildCommands = guild.retrieveCommands().complete();
            DiSky.debug(
                    "Registered: " + registeredCommands.stream().map(BaseCommand::getName).collect(Collectors.toList()) + "\n"
                            + "Instances: " + guildCommands.stream().map(Command::getName).collect(Collectors.toList())
            );

            // Mean the command is already registered / loaded, could need to edit it only
            if (
                    registeredCommands.stream().anyMatch(c -> c.getName().equalsIgnoreCase(cmd.getName()))
                            || guildCommands.stream().anyMatch(c -> c.getName().equalsIgnoreCase(cmd.getName()))
            ) {
                DiSky.debug("Found matching instances / registered, edit way");
                final CommandData registerCommand = registeredCommands
                        .stream()
                        .filter(c -> c.getName().equalsIgnoreCase(cmd.getName()))
                        .findAny()
                        .orElse(null);

                final Command instance = guildCommands
                        .stream()
                        .filter(c -> c.getName().equalsIgnoreCase(cmd.getName()))
                        .findAny()
                        .orElse(null);

                if (instance == null && registerCommand == null)
                    throw new IllegalStateException("Both command instance & input should not be null");

                DiSky.debug("Instance: " + instance.getName() + ", Registered: " + registerCommand.getName());
                if (!instance.getName().equalsIgnoreCase(registerCommand.getName()))
                    throw new IllegalStateException("Both instance & input should have the same name ID");

                DiSky.debug("Are similar? " + isSimilar(command, instance));
                // Mean the command itself is the same, and it only need to reload the Skript code inside.
                if (isSimilar(command, instance))
                    return;

                DiSky.debug("Final edition");
                editCommand(cmd, instance);
            } else {
                DiSky.debug("Didn't found matches, create way enabled");
                addCommand(bot, cmd, false);
            }
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
