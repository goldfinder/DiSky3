package info.itsthesky.disky3.core.skript.slashcommand;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.config.validate.SectionValidator;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.lang.util.SimpleLiteral;
import ch.njol.skript.log.RetainingLogHandler;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.util.StringMode;
import ch.njol.util.StringUtils;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.section.EffectSection;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

            .addEntry("restricted", true)

            .addSection("options", true)

            .addSection("trigger", false);

    public List<SlashArgument> currentArguments;

    private SlashFactory() { }

    public static SlashFactory getInstance() {
        return INSTANCE;
    }

    public boolean add(SectionNode node) {

        this.currentArguments = new ArrayList<>();
        String command = node.getKey();
        if (command == null) {
            return false;
        }

        command = ScriptLoader.replaceOptions(command);
        Matcher matcher = commandPattern.matcher(command);
        if (!matcher.matches()) {
            return false;
        }

        int level = 0;
        for (int i = 0; i < command.length(); i++) {
            if (command.charAt(i) == '[') {
                level++;
            } else if (command.charAt(i) == ']') {
                if (level == 0) {
                    Skript.error("Invalid placement of [optional brackets]");
                    return false;
                }
                level--;
            }
        }
        if (level > 0) {
            Skript.error("Invalid amount of [optional brackets]");
            return false;
        }

        command = matcher.group(2);

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
                return false;
            }

            final SlashArgument argument = new SlashArgument(
                    argumentType,
                    optionals > 0
            );

            if (argument == null)
                return false;
            currentArguments.add(argument);

        }

        node.convertToEntries(0);
        if (!commandStructure.validate(node)) {
            return false;
        }

        if (!(node.get("trigger") instanceof SectionNode))
            return false;

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
                return false;
            }

            options.convertToEntries(0);
            if (!optionListValidator.validate(options))
                return false;

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
                    return false;
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
                        return false;
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

        String restrictedString = ScriptLoader.replaceOptions(node.get("restricted", "false"));
        final boolean restricted;
        try {
            restricted = Boolean.parseBoolean(restrictedString);
        } catch (Exception ex) {
            Skript.error("Wrong value found in the restricted field, should be either 'true' or 'false', but got: " + restrictedString);
            return false;
        }

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
            return false;
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
            return false;
        }

        RetainingLogHandler errors = SkriptLogger.startRetainingLog();
        this.currentArguments = currentArguments;
        final List<TriggerItem> items;
        try {
            items = ScriptLoader.loadItems(trigger);
        } finally {
            EffectSection.stopLog(errors);
        }

        if (guilds.isEmpty() && bots.isEmpty()) {
            Skript.error("Unable to find either bots or guild in the slash command '"+command+"'");
            return false;
        }

        final boolean isGlobal = guilds.isEmpty();
        final CommandData data;
        try {

            data = new CommandData(command, description.getSingle(null))
                    .addOptions(SlashUtils.parseArguments(currentArguments))
                    .setDefaultEnabled(!restricted);
        } catch (Exception ex) {
            DiSky.exception(ex, node);
            return false;
        }

        final String finalCommand = command;
        Bukkit
                .getScheduler()
                .runTaskLater(DiSky.getInstance(), () -> {
                    final List<CommandPrivilege> privileges;
                    privileges = SlashUtils.parsePrivileges(
                            allowedRoles, allowedUsers,
                            disallowedRoles, disallowedUsers
                    );
                    try {
                        if (isGlobal) {
                            for (Bot bot : bots.stream().map(BotManager::searchFromName).collect(Collectors.toList())) {
                                bot
                                        .getCore()
                                        .upsertCommand(data)
                                        .queue(cmd -> SlashObject.register(new SlashObject(
                                                finalCommand, currentArguments, cmd.getId(), bot.getName(), items
                                        )));
                            }
                        } else {
                            List<Guild> parsedGuilds = new ArrayList<>();
                            if (bots.isEmpty()) {
                                parsedGuilds = guilds.stream().map(id -> BotManager.globalSearch(bot -> bot.getCore().getGuildById(id))).collect(Collectors.toList());
                            } else {
                                for (Bot bot : bots.stream().map(BotManager::searchFromName).collect(Collectors.toList())) {
                                    for (String guildID : guilds) {
                                        final Guild guild = bot.getCore().getGuildById(guildID);
                                        if (guild == null)
                                            continue;
                                        parsedGuilds.add(guild);
                                    }
                                }
                            }
                            for (Guild guild : parsedGuilds) {
                                guild
                                        .upsertCommand(data)
                                        .queue(cmd -> {
                                            guild.updateCommandPrivilegesById(cmd.getId(), privileges).queue(ps -> {
                                                for (CommandPrivilege p : ps)
                                                    DiSky.debug("Privilege type " + p.getType().name() + " with input " + p.getId() + " on command " + cmd.getName());
                                            });
                                            SlashObject.register(new SlashObject(
                                                    finalCommand, currentArguments, cmd.getId(),
                                                    BotManager.searchFromJDA(guild.getJDA()).getName(),
                                                    items, guild.getId()
                                            ));
                                        });
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }, 200);

        return true;
    }
}
