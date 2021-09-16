package info.itsthesky.disky3.core.skript.slashcommand;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

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
}
