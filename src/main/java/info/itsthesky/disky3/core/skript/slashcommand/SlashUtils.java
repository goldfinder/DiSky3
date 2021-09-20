package info.itsthesky.disky3.core.skript.slashcommand;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.util.SimpleEvent;
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
        return privileges;
    }

    public static CommandData parseCommand(SlashObject cmd) {
        final Expression<String> desc = cmd.getDescription();
        final String value = desc.getSingle(new SimpleDiSkyEvent<>());
        CommandData command = new CommandData(cmd.getName(), value);

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
