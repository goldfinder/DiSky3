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
                () -> refresh(convert(cmd), bots),
                (alreadyWaited ? 30 : 80)
        );
        if (!alreadyWaited)
            alreadyWaited = true;
        return true;
    }

    public static boolean registerGuilds(SlashObject cmd, List<String> guilds, List<String> roles) {
        Bukkit.getScheduler().runTaskLater(
                DiSky.getInstance(),
                () -> refreshGuilds(convert(cmd), guilds, roles),
                (alreadyWaited ? 30 : 80)
        );
        if (!alreadyWaited)
            alreadyWaited = true;
        return true;
    }

    private static CommandData convert(SlashObject cmd) {
        CommandData command = new CommandData(cmd.getName(), cmd.getDescription());

        for (SlashArgument arg : cmd.getArguments())
        {
            command.addOptions(
                    new OptionData(
                            arg.getType(),
                            arg.getName(),
                            arg.getDesc(),
                            !arg.isOptional()
                    )
            );
        }

        return command;
    }

    private static List<Bot> convert(List<String> s) {
        List<Bot> formattedBots = new ArrayList<>();
        for (String botName : s) {
            if (BotManager.searchFromName(botName) != null)
                formattedBots.add(BotManager.searchFromName(botName));
        }
        return formattedBots;
    }

    private static List<Guild> convertGuilds(List<String> asList) {
        final List<Guild> guilds = new ArrayList<>();
        for (String s : asList) {
            final Guild found;
            if (Utils.isNumeric(s)) {
                found = BotManager.globalSearch(bot -> bot.getCore().getGuildById(s));
            } else {
                found = BotManager.globalSearch(bot -> bot.getCore().getGuildsByName(s, true).get(0));
            }
            if (found != null)
                guilds.add(found);
        }
        return guilds;
    }

    private static List<CommandPrivilege> convertRoles(List<String> asList) {
        final List<CommandPrivilege> guilds = new ArrayList<>();
        for (String s : asList) {
            final Role found;
            if (Utils.isNumeric(s)) {
                found = BotManager.globalSearch(bot -> bot.getCore().getRoleById(s));
            } else {
                found = BotManager.globalSearch(bot -> bot.getCore().getRolesByName(s, true).get(0));
            }
            if (found != null)
                guilds.add(CommandPrivilege.disable(found));
        }
        return guilds;
    }

    private static boolean refreshGuilds(CommandData command, List<String> guilds, List<String> allowedRoles) {
        if (guilds == null || guilds.isEmpty())
            return false;
        final List<CommandPrivilege> privileges = convertRoles(allowedRoles);

        for (Guild guild : convertGuilds(guilds)) {

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
        return true;
    }

    private static boolean refresh(CommandData command, List<String> bots) {
        if (bots == null || bots.isEmpty())
            return false;

        for (Bot bot : convert(bots)) {

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
        return true;
    }

    public static boolean unregister(SlashObject command) {

        for (Bot bot : BotManager.getLoadedBots()) {

            bot.getCore().retrieveCommands().queue(cmds -> {

                for (Command command1 : cmds) {
                    if (!command1.getName().equalsIgnoreCase(command.getName()))
                        continue;
                    command1.delete().queue();
                }

            });

        }

        return true;
    }
}
