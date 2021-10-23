package info.itsthesky.disky3.core.skript.slashcommand;

import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.config.validate.SectionValidator;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.lang.util.SimpleLiteral;
import ch.njol.skript.log.RetainingLogHandler;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.util.StringMode;
import ch.njol.util.StringUtils;
import info.itsthesky.disky3.api.section.EffectSection;
import info.itsthesky.disky3.core.skript.slashcommand.api.SlashArgument;
import info.itsthesky.disky3.core.skript.slashcommand.api.SlashData;
import info.itsthesky.disky3.core.skript.slashcommand.api.SlashObject;
import info.itsthesky.disky3.core.skript.slashcommand.api.register.BotGuildRegister;
import info.itsthesky.disky3.core.skript.slashcommand.api.register.BotRegister;
import info.itsthesky.disky3.core.skript.slashcommand.api.register.GuildRegister;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlashFactory {

    private static final SlashFactory INSTANCE = new SlashFactory();
    private final Pattern commandPattern = Pattern.compile("(?i)^(on )?slash command (\\S+)(\\s+(.+))?$");
    private final Pattern argumentPattern = Pattern.compile("<\\s*(?:(.+?)\\s*?\\s*)?(.+?)\\s*>");
    private final Pattern escape = Pattern.compile("[" + Pattern.quote("(|)<>%\\") + "]");
    private final String listPattern = "\\s*,\\s*|\\s+(and|or|, )\\s+";

    private final SectionValidator optionStructure = new SectionValidator()
            .addEntry("name", false)
            .addEntry("description", false)
            .addSection("default values", true);

    private final SectionValidator presetStructure = new SectionValidator()
            .addEntry("name", false)
            .addEntry("value", false);

    private final SectionValidator commandStructure = new SectionValidator()
            .addEntry("description", false)

            .addEntry("bots", true)
            .addEntry("guilds", true)

            .addEntry("allowed roles", true)
            .addEntry("disallowed roles", true)

            .addEntry("allowed users", true)
            .addEntry("disallowed users", true)

            .addSection("options", true)

            .addSection("trigger", false);

    public HashMap<SlashData, SlashObject> commandMap = new HashMap<>();
    public List<SlashArgument> currentArguments;

    private SlashFactory() { }

    public static SlashFactory getInstance() {
        return INSTANCE;
    }

    public SlashObject add(SectionNode node) {

        this.currentArguments = new ArrayList<>();
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

            String arg = m.group(1) + m.group(2);
            OptionType argumentType;
            try {
                argumentType = OptionType.valueOf(arg.replaceAll(" ", "_").toUpperCase(Locale.ROOT));
            } catch (Exception ex) {
                Skript.error("Can't use " + arg + " as argument of a slash command.");
                return null;
            }

            final SlashArgument argument = new SlashArgument(
                    argumentType,
                    optionals > 0
            );

            if (argument == null)
                return null;
            currentArguments.add(argument);

        }

        node.convertToEntries(0);
        if (!commandStructure.validate(node)) {
            return null;
        }

        if (!(node.get("trigger") instanceof SectionNode))
            return null;

        /*
        Option manager validation
         */
        if (currentArguments.size() > 0) {
            SectionNode options = (SectionNode) node.get("options");

            final SectionValidator optionListValidator = new SectionValidator();
            int i = 1;
            for (SlashArgument arg : currentArguments) {
                optionListValidator.addSection(String.valueOf(i), true);
                i++;
            }

            if (options == null) {
                SkriptLogger.setNode(node);
                Skript.error("You have at least one argument but didn't specified anything for it through the options section.");
                return null;
            }

            options.convertToEntries(0);
            if (!optionListValidator.validate(options))
                return null;

            i = 0;
            for (SlashArgument arg : currentArguments) {
                i++;

                Node node2 = options.get(String.valueOf(i));
                if (node2 == null)
                    continue;
                final SectionNode optionValueNode = (SectionNode) node2;
                if (optionValueNode == null)
                    continue;
                optionValueNode.convertToEntries(0);
                if (!optionStructure.validate(optionValueNode))
                    continue;

                if (optionValueNode.get("name", "").isEmpty() || optionValueNode.get("description", "").isEmpty()) {
                    Skript.error("The entry 'name' and 'description' is require in the " + StringUtils.fancyOrderNumber(i) + " argument options.");
                    return null;
                }

                final @NotNull String name = optionValueNode.get("name", null); // Should not be empty
                final @NotNull String desc = optionValueNode.get("description", null); // Should not be empty

                arg.setName(name);
                arg.setDesc(desc);

                final SectionNode presetListNode = (SectionNode) optionValueNode.get("default values");
                if (presetListNode == null) // default values are optional
                    continue;

                final List<SlashArgument.SlashPreset> presets = new ArrayList<>();
                presetListNode.convertToEntries(0);
                SlashArgument.SlashPreset preset;
                for (int in = 1; in <= 25; in++) {

                    final Node node1 = presetListNode.get(String.valueOf(in));
                    if (node1 == null)
                        continue;
                    if (!(node1 instanceof SectionNode))
                        continue;
                    SectionNode sectionNode = (SectionNode) node1;
                    sectionNode.convertToEntries(0);

                    String presetName = sectionNode.get("name", "");
                    String presetValue = sectionNode.get("value", "");
                    preset = new SlashArgument.SlashPreset(presetName, presetValue, arg);
                    if (preset == null) {
                        Skript.error("Cannot use values " + presetValue + " with argument type " + arg.getType().name().toLowerCase(Locale.ROOT));
                        return null;
                    }
                    presets.add(preset);
                }

                arg.setPresets(presets);
            }
        }

        /*
        Other entry manager
         */
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

        String allowedUserStrings = ScriptLoader.replaceOptions(node.get("allowed users", ""));
        String disallowedUserStrings = ScriptLoader.replaceOptions(node.get("disallowed users", ""));
        List<String> allowedUsers = allowedUserStrings.isEmpty() ? new ArrayList<>() : Arrays.asList(allowedUserStrings.split(listPattern));
        List<String> disallowedUsers = disallowedUserStrings.isEmpty() ? new ArrayList<>() : Arrays.asList(disallowedUserStrings.split(listPattern));

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
                    allowedRoles, disallowedRoles,
                    allowedUsers, disallowedUsers
            );
        } finally {
            EffectSection.stopLog(errors);
        }

        try {
            if (!guilds.isEmpty() && bots.isEmpty()) {

                GuildRegister.getInstance().registerCommand(guilds, slashObject);

            } else if (!guilds.isEmpty() && !bots.isEmpty()) {

                BotGuildRegister.getInstance().registerCommand(guilds, bots, slashObject);

            } else if (!bots.isEmpty()) {

                BotRegister.getInstance().registerCommand(bots, slashObject);

            } else {
                Skript.error("Unable to get either bots or guilds to register the slash command on.");
                return null;
            }
        } catch (Exception ex) {}

        this.commandMap.put(new SlashData(command, slashObject), slashObject);
        return slashObject;
    }

    public boolean remove(String name) {
        for (SlashData commandData : commandMap.keySet()) {
            SlashObject commandObject = commandData.getCommand();
            if (commandObject.getName().equalsIgnoreCase(name)) {

                if (commandObject.isGlobalRegister()) {
                    GuildRegister.getInstance().unregister(
                            commandObject.getGuilds(),
                            commandObject
                    );
                } else {
                    BotRegister.getInstance().unregister(
                            commandObject.getBots(),
                            commandObject
                    );
                }
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
