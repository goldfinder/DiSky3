package info.itsthesky.disky3.core.skript.slashcommand;

import ch.njol.skript.Skript;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public final class SlashManager {

    private static boolean alreadyWaited = false;

    public static boolean register(SlashObject cmd, List<String> bots) {
        Bukkit.getScheduler().runTaskLater(
                DiSky.getInstance(),
                () -> refresh(cmd, bots),
                (alreadyWaited ? 30 : 80)
        );
        if (!alreadyWaited)
            alreadyWaited = true;
        return true;
    }

    public static void registerGuilds(SlashObject cmd, List<String> guilds) {
        Bukkit.getScheduler().runTaskLater(
                DiSky.getInstance(),
                () -> refreshGuilds(cmd, guilds),
                (alreadyWaited ? 30 : 80)
        );
        if (!alreadyWaited)
            alreadyWaited = true;
    }

    private static void refreshGuilds(SlashObject cmd, List<String> guilds) {
        if (guilds == null || guilds.isEmpty())
            return;
        CommandData command = SlashUtils.parseCommand(cmd);
        List<CommandPrivilege> privileges = SlashUtils.parsePrivileges(cmd);

        for (Guild guild : SlashUtils.parseGuilds(guilds)) {

            guild.retrieveCommands().queue(cmds -> {

                Command registered = null;
                DiSky.debug("Starting registering " + command.getName());
                for (Command command1 : cmds) {
                    if (command1.getName().equalsIgnoreCase(command.getName()))
                        registered = command1;
                }
                final boolean shouldEdit = registered != null;

                // Mean we can just edit the command
                DiSky.debug("Should edit: " + shouldEdit);
                if (shouldEdit) {

                    DiSky.debug("Editing slash command " + command.getName());
                    registered
                            .editCommand()
                            .apply(command)
                            .queue(refreshedCommand -> {
                                guild.updateCommandPrivilegesById(
                                        refreshedCommand.getId(),
                                        privileges
                                ).queue();
                            });

                } else {

                    CommandListUpdateAction action = guild.updateCommands();
                    action.addCommands(command).queue(
                            cmds2 -> {
                                for (Command cmd1 : cmds2)
                                {
                                    if (command.getName().equalsIgnoreCase(cmd1.getName()))
                                        guild.updateCommandPrivilegesById(
                                                cmd1.getId(),
                                                privileges
                                        ).queue();
                                }
                            }
                    );

                }
            });

        }
    }

    private static void refresh(SlashObject cmd, List<String> bots) {
        if (bots == null || bots.isEmpty())
            return;
        CommandData command = SlashUtils.parseCommand(cmd);
        List<CommandPrivilege> privileges = SlashUtils.parsePrivileges(cmd);

        for (Bot bot : SlashUtils.parseBots(bots)) {

            bot.getCore().retrieveCommands().queue(cmds -> {

                Command registered = null;
                DiSky.debug("Starting registering " + command.getName());
                for (Command command1 : cmds) {
                    if (command1.getName().equalsIgnoreCase(command.getName()))
                        registered = command1;
                }
                final boolean shouldEdit = registered != null;

                // Mean we can just edit the command
                DiSky.debug("Should edit: " + shouldEdit);
                if (shouldEdit) {

                    DiSky.debug("Editing slash command " + command.getName());
                    registered
                            .editCommand()
                            .apply(command)
                            .queue();

                } else {

                    CommandListUpdateAction action = bot.getCore().updateCommands();
                    action.addCommands(command).queue();

                }

            });

        }
    }

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
