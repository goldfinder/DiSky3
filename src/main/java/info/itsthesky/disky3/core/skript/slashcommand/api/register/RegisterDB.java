package info.itsthesky.disky3.core.skript.slashcommand.api.register;

import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.core.commands.Argument;
import info.itsthesky.disky3.core.skript.slashcommand.api.SlashArgument;
import info.itsthesky.disky3.core.skript.slashcommand.api.SlashObject;
import info.itsthesky.disky3.core.skript.slashcommand.api.SlashUtils;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.BaseCommand;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandEditAction;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public abstract class RegisterDB {

    public HashMap<String, HashMap<String, CommandData>> DATA_BASE;

    public RegisterDB(String name) {
        DATA_BASE = new HashMap<>();
        DiSky.log("Registering a new slash command storing way: " + name);
    }

    protected boolean addCommand(Bot bot, CommandData o, boolean force) {
        final HashMap<String, CommandData> current = getCommands(bot);

        if (!force && current.values().stream().anyMatch(cmd -> cmd.getName().equalsIgnoreCase(o.getName())))
        {
            DiSky.debug("Removing old instance of " + o.getName() + " command since it was already existing.");
            return false;
        }

        current.put(o.getName(), o);
        setCommand(bot, current.values());
        DiSky.debug("Adding command to DB: " + o.getName());
        DiSky.debug("Now having: " +
                        getCommands(bot).values().stream().map(BaseCommand::getName).collect(Collectors.toList())
                );
        return true;
    }

    protected boolean isSimilar(SlashObject cmd, Command second) {
        final CommandData first = SlashUtils.parseCommand(cmd);
        if (!first.getName().equalsIgnoreCase(second.getName()))
            return false;
        if (!first.getDescription().equalsIgnoreCase(second.getDescription()))
            return false;
        if (!first.getOptions().containsAll(SlashUtils.parseOptions(second.getOptions())))
            return false;
        return true;
    }

    protected boolean isSimilar(SlashObject cmd, CommandData second) {
        final CommandData first = SlashUtils.parseCommand(cmd);
        if (!first.getName().equalsIgnoreCase(second.getName()))
            return false;
        if (!first.getDescription().equalsIgnoreCase(second.getDescription()))
            return false;
        if (!first.getOptions().containsAll(second.getOptions()))
            return false;
        return true;
    }

    protected boolean alreadyRegistered(Bot bot, CommandData os) {
        boolean value = getCommands(bot).values().stream().anyMatch(os1 -> os1.getName().equalsIgnoreCase(os.getName()));
        DiSky.debug(os.getName() + " " + value);
        return value;
    }

    protected void setCommand(Bot bot, Collection<CommandData> os) {
        final HashMap<String, CommandData> currentData =
                DATA_BASE.get(bot.getName()) == null ? new HashMap<>() : DATA_BASE.get(bot.getName());
        final HashMap<String, CommandData> newData = new HashMap<>();
        os.forEach(cmd -> newData.put(cmd.getName(), cmd));

        currentData.putAll(newData);
        DATA_BASE.put(bot.getName(), currentData);
    }

    protected CommandEditAction editCommand(CommandData data, Command original) {
        return original.editCommand().apply(data);
    }

    protected void clearCommands(Bot bot) {
        DATA_BASE.remove(bot.getName());
    }

    protected HashMap<String, CommandData> getCommands(Bot bot) {
        return DATA_BASE.get(bot.getName()) == null ? new HashMap<>() : DATA_BASE.get(bot.getName());
    }
}
