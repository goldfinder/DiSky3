package info.itsthesky.disky3.api;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.command.Argument;
import ch.njol.skript.config.Config;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.RetainingLogHandler;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.Date;
import ch.njol.skript.util.Getter;
import ch.njol.skript.util.SkriptColor;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.emojis.EmojiManager;
import info.itsthesky.disky3.api.emojis.EmojiParser;
import info.itsthesky.disky3.api.emojis.Emote;
import info.itsthesky.disky3.api.wrapper.ButtonRow;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public static boolean containInterface(Class<?> clazz, Class<?> inter) {
        return Arrays.asList(clazz.getInterfaces()).contains(inter);
    }

    public static <T> void setSkriptList(Variable<T> variable, Event event, List<?> values) {
        List<Object> list = Collections.singletonList(values);
        String name = variable.getName().getDefaultVariableName();

        int separatorLength = Variable.SEPARATOR.length() + 1;
        name = name.substring(0, (name.length() - separatorLength));
        name = name.toLowerCase() + Variable.SEPARATOR;
        for (int i = 1; i < list.size()+1; i++){
            Variables.setVariable(name + i, list.get(i-1), event, variable.isLocal());
        }
    }

    public static <T> T[] verifyVars(@NotNull Event e, @Nullable Expression<T> expression, T[] defaultValue) {
        return expression == null ? defaultValue : (expression.getArray(e) == null ? defaultValue : expression.getArray(e));
    }

    public static boolean isBetween(Number value, Number min, Number max) {
        return value.doubleValue() >= min.doubleValue() && value.doubleValue() <= max.doubleValue();
    }

    public static Emoji convert(Emote emote) {
        return emote.isEmote() ? Emoji.fromEmote(emote.getEmote()) : Emoji.fromUnicode(emote.getAsMention());
    }

    public static List<ActionRow> convert(List<Object> items) {
        final List<ActionRow> rows = new ArrayList<>();
        for (Object i : items) {
            ActionRow row = (i instanceof ActionRow ? (ActionRow) i : ActionRow.of((SelectionMenu) i));
            rows.add(row);
        }
        return rows;
    }

    public static Date convert(OffsetDateTime time) {
        return new Date(time.toInstant().toEpochMilli(), TimeZone.getTimeZone("GMT"));
    }

    public static boolean isNumeric(String s) {
        return s.matches("-?\\d+(\\.\\d+)?");
    }

    public static <T> Expression<T> verifyDefaultToEvent(
            Expression<?> expr, Expression<T> exprSomething, Class<T> entityClass
    ) {
        if (expr == null) {
            return defaultToEventValue(exprSomething, entityClass);
        } else {
            return (Expression<T>) expr;
        }
    }

    public static <T, F> T tryOrDie(Function<F, T> function, F instance, T defaultValue) {
        try {
            return function.apply(instance);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public static String now() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static boolean isURL(String url) {
        try {
            final URL url1 = new URL(url);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean skImageInstalled() {
        try {
            Class.forName("info.itsthesky.SkImage.SkImage");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean equalsAnyIgnoreCase(String toMatch, String... potentialMatches) {
        return Arrays.asList(potentialMatches).contains(toMatch);
    }

    public static Color convert(org.bukkit.Color original) {
        return SkriptColor.fromBukkitColor(original);
    }

    public static SkriptColor convert(java.awt.Color original) {
        return SkriptColor.fromBukkitColor(org.bukkit.Color.fromRGB(original.getRGB()));
    }

    public static Emote unicodeFrom(String input, Guild guild) {
        String id = input.replaceAll("[^0-9]", "");
        if (id.isEmpty()) {
            try {
                if (guild == null) {
                    Set<JDA> jdaInstances = BotManager.getBotsJDA();
                    for (JDA jda : jdaInstances) {
                        Collection<net.dv8tion.jda.api.entities.Emote> emoteCollection = jda.getEmotesByName(input, false);
                        if (!emoteCollection.isEmpty()) {
                            return new Emote(emoteCollection.iterator().next());
                        }
                    }
                    return unicodeFrom(input);
                }
                Collection<net.dv8tion.jda.api.entities.Emote> emotes = guild.getEmotesByName(input, false);
                if (emotes.isEmpty()) {
                    return unicodeFrom(input);
                }

                return new Emote(emotes.iterator().next());
            } catch (UnsupportedOperationException | NoSuchElementException x) {
                return null;
            }
        } else {
            if (guild == null) {
                Set<JDA> jdaInstances = BotManager.getBotsJDA();
                for (JDA jda : jdaInstances) {
                    net.dv8tion.jda.api.entities.Emote emote = jda.getEmoteById(id);
                    if (emote != null) {
                        return new Emote(emote);
                    }
                }
                return unicodeFrom(input);
            }
            try {
                net.dv8tion.jda.api.entities.Emote emote = guild.getEmoteById(id);
                if (emote == null) {
                    net.dv8tion.jda.api.entities.Emote emote1 = guild.getJDA().getEmoteById(id);
                    if (!(emote1 == null)) {
                        return new Emote(emote1);
                    }
                    return null;
                }

                return new Emote(emote);
            } catch (UnsupportedOperationException | NoSuchElementException x) {
                return null;
            }
        }
    }

    public static Emote unicodeFrom(String input) {
        if (EmojiManager.isEmoji(input)) {
            return new Emote(input, EmojiParser.parseToUnicode(input));
        } else {
            String emote = input.contains(":") ? input : ":" + input + ":";
            return new Emote(input.replaceAll(":", ""), EmojiParser.parseToUnicode(emote));
        }
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

    public static User parseUser(Object o) {
        return o instanceof Member ? ((Member) o).getUser() : (User) o;
    }

    public static MessageChannel parseMessageChannel(Object content) {

        if (content == null)
            return null;

        if (content instanceof GuildChannel && ((GuildChannel) content).getType().equals(ChannelType.TEXT)) {
            return (TextChannel) content;
        } else if (content instanceof TextChannel) {
            return (TextChannel) content;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> Expression<T> defaultToEventValue(Expression<T> expr, Class<T> clazz) {
        if (expr != null)
            return expr;
        Class<? extends Event>[] events = ScriptLoader.getCurrentEvents();
        for (Class<? extends Event> e : events == null ? new Class[0] : events) {
            Getter getter = EventValues.getEventValueGetter(e, clazz, 0);
            if (getter != null) {
                return new SimpleExpression<T>() {
                    @Override
                    protected T[] get(Event e) {
                        T value = (T) getter.get(e);
                        if (value == null)
                            return null;
                        T[] arr = (T[]) Array.newInstance(clazz, 1);
                        arr[0] = value;
                        return arr;
                    }

                    @Override
                    public boolean isSingle() {
                        return true;
                    }

                    @Override
                    public Class<? extends T> getReturnType() {
                        return clazz;
                    }

                    @Override
                    public boolean isDefault() {
                        return true;
                    }

                    @Override
                    public String toString(Event e, boolean debug) {
                        return "defaulted event value expression";
                    }

                    @Override
                    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
                        return true;
                    }
                };
            }
        }
        return null;
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
            if (guild.getMemberById(id) != null)
                return guild.getMemberById(id);
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

    public static ActionRow[] parseComponents(Object[] vars) {
        final List<ActionRow> rows = new ArrayList<>();
        for (Object o : vars) {
            if (o instanceof ButtonRow) {
                rows.add(
                        ActionRow.of(((ButtonRow) o).getButtons())
                );
            } else {
                rows.add(
                        ActionRow.of(((SelectionMenu.Builder) o).build())
                );
            }
        }
        return rows.toArray(new ActionRow[0]);
    }
}
