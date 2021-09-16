package info.itsthesky.disky3.core.skript.slashcommand;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.config.validate.SectionValidator;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.log.RetainingLogHandler;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.util.StringUtils;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.EffectSection;
import info.itsthesky.disky3.core.commands.Argument;
import info.itsthesky.disky3.core.commands.CommandObject;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlashFactory {

    private static final SlashFactory INSTANCE = new SlashFactory();
    private final Pattern commandPattern = Pattern.compile("(?i)^(on )?slash command (\\S+)(\\s+(.+))?$");
    private final Pattern argumentPattern = Pattern.compile("<\\s*(?:(.+?)\\s*:\\s*)?(.+?)\\s*(?:=\\s*(" + SkriptParser.wildcard + "))?\\s*>");
    private final Pattern escape = Pattern.compile("[" + Pattern.quote("(|)<>%\\") + "]");
    private final String listPattern = "\\s*,\\s*|\\s+(and|or|, )\\s+";

    private final SectionValidator commandStructure = new SectionValidator()
            .addEntry("description", true)
            .addEntry("bots", false)
            .addSection("trigger", false);

    public HashMap<SlashData, SlashObject> commandMap = new HashMap<>();
    public List<SlashArgument> currentArguments;

    private SlashFactory() { }

    public static SlashFactory getInstance() {
        return INSTANCE;
    }

    private String escape(final String s) {
        return "" + escape.matcher(s).replaceAll("\\\\$0");
    }

    public SlashObject add(SectionNode node) {

        String command = node.getKey();
        if (command == null) {
            return null;
        }

        command = ScriptLoader.replaceOptions(command);
        Matcher matcher = commandPattern.matcher(command);
        if (!matcher.matches()) {
            return null;
        }

        int level = 0;
        for (int i = 0; i < command.length(); i++) {
            if (command.charAt(i) == '[') {
                level++;
            } else if (command.charAt(i) == ']') {
                if (level == 0) {
                    Skript.error("Invalid placement of [optional brackets]");
                    return null;
                }
                level--;
            }
        }
        if (level > 0) {
            Skript.error("Invalid amount of [optional brackets]");
            return null;
        }

        command = matcher.group(2);

        for (SlashData storage : this.commandMap.keySet()) {
            SlashObject commandObject = storage.getCommand();
            if (commandObject.getName().equalsIgnoreCase(command)) {
                if (commandObject.getScript().equals(node.getConfig().getFile())) {
                    Skript.error("A slash command with the name \"" + command + "\" is already defined in " + commandObject.getScript().getName());
                }
            }
        }

        String arguments = matcher.group(4);
        if (arguments == null) {
            arguments = "";
        }

        List<SlashArgument> currentArguments = this.currentArguments = new ArrayList<>();
        Matcher m = argumentPattern.matcher(arguments);
        int lastEnd = 0;
        int optionals = 0;

        for (int i = 0; m.find(); i++) {
            optionals += StringUtils.count(arguments, '[', lastEnd, m.start());
            optionals -= StringUtils.count(arguments, ']', lastEnd, m.start());

            lastEnd = m.end();

            String arg = m.group(2);
            OptionType argumentType;
            try {
                argumentType = OptionType.valueOf(arg.replaceAll(" ", "_").toUpperCase(Locale.ROOT));
            } catch (Exception ex) {
                Skript.error("Can't use " + arg + " as argument of a slash command.");
                return null;
            }

            @Nullable String desc = m.group(3);

            final SlashArgument argument = new SlashArgument(
                    argumentType,
                    optionals > 0,
                    desc
            );

            if (argument == null)
                return null;
            currentArguments.add(argument);

        }

        node.convertToEntries(0);
        if (!commandStructure.validate(node)) {
            return null;
        }

        if (!(node.get("trigger") instanceof SectionNode)) {
            return null;
        }

        SectionNode trigger = (SectionNode) node.get("trigger");
        String description = ScriptLoader.replaceOptions(node.get("description", ""));
        List<String> aliases = Arrays.asList(ScriptLoader.replaceOptions(node.get("aliases", "")).split(listPattern));

        String botString = ScriptLoader.replaceOptions(node.get("bots", ""));
        List<String> bots = botString.isEmpty() ? new ArrayList<>() : Arrays.asList(botString.split(listPattern));

        RetainingLogHandler errors = SkriptLogger.startRetainingLog();
        SlashObject slashObject;
        this.currentArguments = currentArguments;
        try {
            slashObject = new SlashObject(
                    node.getConfig().getFile(), command, currentArguments, aliases,
                    description, bots, ScriptLoader.loadItems(trigger)
            );
        } finally {
            EffectSection.stopLog(errors);
        }

        SlashManager.register(slashObject, bots);

        this.commandMap.put(new SlashData(command, slashObject), slashObject);
        return slashObject;
    }

    public boolean remove(String name) {
        for (SlashData commandData : commandMap.keySet()) {
            SlashObject commandObject = commandData.getCommand();
            if (commandObject.getName().equalsIgnoreCase(name)) {

                SlashManager.unregister(commandObject);
                commandMap.remove(commandData);
                return true;
            }
        }
        return false;
    }

    public Collection<SlashData> getCommands() {
        return commandMap.keySet();
    }
}
