package info.itsthesky.disky3.core.skript.slashcommand;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.config.validate.SectionValidator;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.lang.util.SimpleLiteral;
import ch.njol.skript.log.RetainingLogHandler;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.util.StringMode;
import ch.njol.util.NonNullPair;
import ch.njol.util.StringUtils;
import info.itsthesky.disky3.api.skript.EffectSection;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlashFactory {

    private static final SlashFactory INSTANCE = new SlashFactory();
    private final Pattern commandPattern = Pattern.compile("(?i)^(on )?slash command (\\S+)(\\s+(.+))?$");
    private final Pattern argumentPattern = Pattern.compile("<\\s*(?:(.+?)\\s*?\\s*)?(.+?)\\s*(?:=\\s*([^\"]*?(?:\"[^\"]*?\"[^\"]*?)*?))?\\s*>");
    private final Pattern escape = Pattern.compile("[" + Pattern.quote("(|)<>%\\") + "]");
    private final String listPattern = "\\s*,\\s*|\\s+(and|or|, )\\s+";

    private final SectionValidator commandStructure = new SectionValidator()
            .addEntry("description", false)
            .addEntry("bots", true)
            .addEntry("guilds", true)
            .addEntry("allowed roles", true)
            .addEntry("disallowed roles", true)
            .addSection("trigger", false);

    public HashMap<SlashData, SlashObject> commandMap = new HashMap<>();
    public List<SlashArgument> currentArguments;

    private SlashFactory() { }

    public static SlashFactory getInstance() {
        return INSTANCE;
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

            // Because
            String arg = m.group(1) + m.group(2);
            OptionType argumentType;
            try {
                argumentType = OptionType.valueOf(arg.replaceAll(" ", "_").toUpperCase(Locale.ROOT));
            } catch (Exception ex) {
                Skript.error("Can't use " + arg + " as argument of a slash command.");
                return null;
            }

            final String name;
            final String desc;

            final String infoInput = m.group(3);
            if (infoInput.matches("\\{(.+)}")) {
                final NonNullPair<String, String> values = SlashArgument.parseArgumentValues(
                        infoInput.replaceAll("\\{", "").replaceAll("}", "")
                );
                if (values == null) {
                    Skript.error("Unable to match the name & description pattern. It MUST be under '\"name\", \"desc\"' form!");
                    return null;
                }
                name = values.getFirst();
                desc = values.getSecond();
            } else {
                name = argumentType.name().toLowerCase(Locale.ROOT).replaceAll("_", " ");
                desc = infoInput;
            }

            final SlashArgument argument = new SlashArgument(
                    argumentType,
                    optionals > 0,
                    desc,
                    name
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
        List<String> aliases = Arrays.asList(ScriptLoader.replaceOptions(node.get("aliases", "")).split(listPattern));

        String botString = ScriptLoader.replaceOptions(node.get("bots", ""));
        List<String> bots = botString.isEmpty() ? new ArrayList<>() : Arrays.asList(botString.split(listPattern));

        String guildString = ScriptLoader.replaceOptions(node.get("guilds", ""));
        List<String> guilds = guildString.isEmpty() ? new ArrayList<>() : Arrays.asList(guildString.split(listPattern));

        String allowedRoleStrings = ScriptLoader.replaceOptions(node.get("allowed roles", ""));
        String disallowedRoleStrings = ScriptLoader.replaceOptions(node.get("disallowed roles", ""));
        List<String> allowedRoles = allowedRoleStrings.isEmpty() ? new ArrayList<>() : Arrays.asList(allowedRoleStrings.split(listPattern));
        List<String> disallowedRoles = disallowedRoleStrings.isEmpty() ? new ArrayList<>() : Arrays.asList(disallowedRoleStrings.split(listPattern));

        // Added the parse to the command description
        Expression<String> description;
        String rawDesc = ScriptLoader.replaceOptions(node.get("description", ""));
        if (rawDesc.isEmpty()) {
            Skript.error("The command description cannot be empty!");
            return null;
        }

        // Unboxing the expression if people put ""
        if (rawDesc.startsWith("\"") && rawDesc.endsWith("\""))
            rawDesc = rawDesc.substring(1, rawDesc.length() - 1);

        // Parsing the raw description to an usable expression
        description = VariableString.newInstance(rawDesc, StringMode.MESSAGE);
        try {
            if (((VariableString) description).isSimple())
                description = new SimpleLiteral<>(rawDesc, false);
        } catch (NullPointerException ignored) { }
        if (description == null)
        {
            Skript.error("The command description cannot be null (this normally can't happen)");
            return null;
        }

        RetainingLogHandler errors = SkriptLogger.startRetainingLog();
        SlashObject slashObject;
        this.currentArguments = currentArguments;
        try {
            slashObject = new SlashObject(
                    node.getConfig().getFile(), command, currentArguments, aliases,
                    description, bots, ScriptLoader.loadItems(trigger), guilds,
                    allowedRoles, disallowedRoles
            );
        } finally {
            EffectSection.stopLog(errors);
        }

        if (!guilds.isEmpty()) {
            SlashManager.registerGuilds(slashObject, guilds);
        } else if (!bots.isEmpty()) {
            SlashManager.register(slashObject, bots);
        } else {
            Skript.error("Unable to get either bots or guilds to register the slash command on.");
            return null;
        }

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
