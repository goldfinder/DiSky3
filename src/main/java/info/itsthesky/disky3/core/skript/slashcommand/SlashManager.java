package info.itsthesky.disky3.core.skript.slashcommand;

import ch.njol.skript.Skript;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
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

    private static CommandData convert(SlashObject cmd) {
        CommandData command = new CommandData(cmd.getName(), cmd.getDescription());

        for (SlashArgument arg : cmd.getArguments())
        {
            command.addOptions(
                    new OptionData(
                            arg.getType(),
                            arg.getName(),
                            arg.getDesc()
                    )
                            .setRequired(!arg.isOptional())
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

                    if (!shouldEdit(registered, command))
                    {
                        DiSky.debug("Should not edit slash because name & desc are same.");
                        return;
                    }

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

    public static boolean shouldEdit(Command first, CommandData second) {
        return !first.getName().equalsIgnoreCase(second.getName()) ||
                !first.getDescription().equalsIgnoreCase(second.getDescription());
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
