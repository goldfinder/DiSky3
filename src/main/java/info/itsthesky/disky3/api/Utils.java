package info.itsthesky.disky3.api;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Variable;
import info.itsthesky.disky3.DiSky;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * All these code were made & are owned by ItsTheSky!
 * @author ItsTheSky
 */
public final class Utils {

    public static String colored(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static <T> T verifyVar(@NotNull Event e, @Nullable Expression<T> expression) {
        return verifyVar(e, expression, null);
    }

    public static <T> T verifyVar(@NotNull Event e, @Nullable Expression<T> expression, T defaultValue) {
        return expression == null ? defaultValue : (expression.getSingle(e) == null ? defaultValue : expression.getSingle(e));
    }

    public static boolean equalsAnyIgnoreCase(String toMatch, String... potentialMatches) {
        return Arrays.asList(potentialMatches).contains(toMatch);
    }

    public static <F, T> T[] convertArray(
            Function<F, T> function,
            F[] list
    ) {
        List<T> ts = new ArrayList<>();
        for (F f : list) {
            ts.add(function.apply(f));
        }
        return ts.toArray((T[]) new Object[0]);
    }

    public static MessageBuilder parseMessageContent(Object content) {

        if (content == null)
            return null;

        MessageBuilder builder = new MessageBuilder();
        if (content instanceof EmbedBuilder) {
            builder.setEmbeds(((EmbedBuilder) content).build());
        } else if (content instanceof MessageBuilder) {
            builder = (MessageBuilder) content;
        } else {
            builder.append(content.toString());
        }
        return builder;
    }

    @Nullable
    public static Long parseLong(@NotNull String s, boolean manageDiscordValue) {
        return parseLong(s, false, manageDiscordValue);
    }

    @Nullable
    public static Long parseLong(@NotNull String s, boolean shouldPrintError, boolean manageDiscordValue) {
        Long id = null;
        if (manageDiscordValue) {
            s = s
                    .replaceAll("&", "")
                    .replaceAll("<", "")
                    .replaceAll(">", "")
                    .replaceAll("#", "")
                    .replaceAll("!", "")
                    .replaceAll("@", "");
        }
        try {
            id = Long.parseLong(s);
        } catch (NumberFormatException e) {
            //if (shouldPrintError) e.printStackTrace();
            return null;
        }
        return id;
    }

    @Nullable
    public static Member searchMember(JDA bot, String id) {
        for (Guild guild : bot.getGuilds()) {
            for (Member member : guild.getMembers()) {
                if (member.getId().equalsIgnoreCase(id)) return member;
            }
        }
        return null;
    }

    public static @Nullable Variable<?> parseVar(Expression<?> expression, boolean shouldBeList, boolean showError) {
        if (expression instanceof Variable<?>) {
            if (shouldBeList && !((Variable<?>) expression).isList()) {
                if (showError)
                    Skript.error("The specified variable must be a list!");
                return null;
            }
            return (Variable<?>) expression;
        }
        if (showError)
            Skript.error("You must specific a valid variable, but got " + expression);
        return null;
    }

    public static List<Permission> convertPerms(String... perms) {
        List<Permission> permissions = new ArrayList<>();
        for (String s : perms) {
            try {
                permissions.add(Permission.valueOf(s.replace(" ", "_").toUpperCase()));
            } catch (IllegalArgumentException ignored) {}
        }
        return permissions;
    }

    public static void sync(Runnable runnable) {
        Bukkit.getScheduler().runTask(DiSky.getInstance(), runnable);
    }

    public static void async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(DiSky.getInstance(), runnable);
    }

    public static @Nullable Variable<?> parseVar(Expression<?> expression, boolean showError) {
        return parseVar(expression, false, showError);
    }

}
