package info.itsthesky.disky3.core.skript.slashcommand.api.register;

import info.itsthesky.disky3.api.bot.Bot;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandEditAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class RegisterDB {

    protected static HashMap<Bot, List<CommandData>> DATA_BASE = new HashMap<>();

    protected static void addCommand(Bot bot, CommandData o) {
        final List<CommandData> current = getCommands(bot);
        if (!current.contains(o))
            current.add(o);
        setCommand(bot, current);
    }

    protected static void setCommand(Bot bot, List<CommandData> os) {
        DATA_BASE.put(bot, os);
    }

    protected static void removeCommand(Bot bot, CommandData o) {
        final List<CommandData> current = getCommands(bot);
        current.remove(o);
        setCommand(bot, current);
    }

    protected static CommandEditAction editCommand(CommandData data, Command original) {
        return original.editCommand().apply(data);
    }

    protected static void clearCommands(Bot bot) {
        DATA_BASE.remove(bot);
    }

    protected static List<CommandData> getCommands(Bot bot) {
        return DATA_BASE.get(bot) == null ? new ArrayList<>() : DATA_BASE.get(bot);
    }

}
