package info.itsthesky.disky3.core.skript.slashcommand.api;

import ch.njol.skript.lang.Expression;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class SlashUtils {

    public static Class<?> convert(OptionType type) {
        switch (type) {
            case USER:
                return User.class;
            case ROLE:
                return Role.class;
            case STRING:
                return String.class;
            case BOOLEAN:
                return Boolean.class;
            case INTEGER:
                return Integer.class;
            case CHANNEL:
                return GuildChannel.class;
            default:
                return Object.class;
        }
    }

    public static Object convert(OptionMapping option) {
        switch (option.getType()) {
            case USER:
                return option.getAsUser();
            case ROLE:
                return option.getAsRole();
            case STRING:
                return option.getAsString();
            case BOOLEAN:
                return option.getAsBoolean();
            case INTEGER:
                return option.getAsLong();
            case CHANNEL:
                return option.getAsGuildChannel();
            default:
                return null;
        }
    }

    public static List<Bot> parseBots(List<String> s) {
        List<Bot> formattedBots = new ArrayList<>();
        for (String botName : s) {
            if (BotManager.searchFromName(botName) != null)
                formattedBots.add(BotManager.searchFromName(botName));
        }
        return formattedBots;
    }

    public static List<CommandPrivilege> parsePrivileges(SlashObject cmd) {
        final List<CommandPrivilege> privileges = new ArrayList<>();
        for (Role a : parseRoles(cmd.getAllowedRoles()))
            privileges.add(CommandPrivilege.enable(a));
        for (Role d : parseRoles(cmd.getDisallowedRoles()))
            privileges.add(CommandPrivilege.disable(d));
        for (User d : parseUsers(cmd.getAllowedUsers()))
            privileges.add(CommandPrivilege.enable(d));
        for (User d : parseUsers(cmd.getDisallowedUsers()))
            privileges.add(CommandPrivilege.disable(d));
        return privileges;
    }

    public static CommandData parseCommand(SlashObject cmd) {
        try {
            final Expression<String> desc = cmd.getDescription();
            final String value = desc.getSingle(new SimpleDiSkyEvent<>());
            CommandData command = new CommandData(cmd.getName(), value);

            for (SlashArgument arg : cmd.getArguments()) {
                OptionData option = new OptionData(
                        arg.getType(),
                        arg.getName(),
                        arg.getDesc(),
                        !arg.isOptional()
                );
                for (SlashArgument.SlashPreset o : arg.getPresets()) {
                    if (arg.getType().equals(OptionType.STRING)) {
                        option.addChoice(o.getName(), o.getAsText());
                    } else if (arg.getType().equals(OptionType.INTEGER)) {
                        option.addChoice(o.getName(), o.getAsInt());
                    }
                }
                command.addOptions(
                        option
                );
            }

            return command;
        } catch (Exception ex ) {
            DiSky.exception(ex, null);
            return null;
        }
    }

    public static List<Guild> parseGuilds(List<String> asList) {
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

    public static boolean compareOption(Command.Option loaded, OptionData unLoaded) {
        return
                loaded.getName().equalsIgnoreCase(unLoaded.getName()) &&
                        loaded.getDescription().equalsIgnoreCase(unLoaded.getDescription()) &&
                        loaded.getType().equals(unLoaded.getType()) &&
                        loaded.getChoices().containsAll(unLoaded.getChoices());
    }

    public static OptionData parseOption(Command.Option original) {
        return new OptionData(original.getType(), original.getName(), original.getDescription())
                .setRequired(original.isRequired());
    }

    public static boolean compareCommand(Command loaded, CommandData unLoaded) {
        return
                loaded.getName().equalsIgnoreCase(unLoaded.getName()) &&
                        loaded.getDescription().equalsIgnoreCase(unLoaded.getDescription()) &&
                        loaded
                                .getOptions()
                                .stream()
                                .map(SlashUtils::parseOption)
                                .collect(Collectors.toList())
                                .containsAll(unLoaded.getOptions());
    }

    public static List<User> parseUsers(List<String> asList) {
        final List<User> guilds = new ArrayList<>();
        for (String s : asList) {
            final User found;
            // We assume the target is already cached by any way so don't need to retrieve it.
            if (Utils.isNumeric(s)) {
                found = BotManager.globalSearch(bot -> bot.getCore().getUserById(s));
            } else {
                found = BotManager.globalSearch(bot -> bot.getCore().getUsersByName(s, true).get(0));
            }
            if (found != null)
                guilds.add(found);
        }
        return guilds;
    }

    public static List<Role> parseRoles(List<String> asList) {
        final List<Role> guilds = new ArrayList<>();
        for (String s : asList) {
            final Role found;
            if (Utils.isNumeric(s)) {
                found = BotManager.globalSearch(bot -> bot.getCore().getRoleById(s));
            } else {
                found = BotManager.globalSearch(bot -> bot.getCore().getRolesByName(s, true).get(0));
            }
            if (found != null)
                guilds.add(found);
        }
        return guilds;
    }
}
