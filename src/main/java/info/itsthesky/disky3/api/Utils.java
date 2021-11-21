package info.itsthesky.disky3.api;

import ch.njol.skript.util.*;
import ch.njol.skript.util.Date;
import info.itsthesky.disky3.api.emojis.updated.Emojis;
import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.aliases.Aliases;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.lang.util.SimpleLiteral;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.emojis.Emote;
import info.itsthesky.disky3.api.wrapper.ButtonRow;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    public static String getName(Object name) {
        try {
            if (name instanceof Member)
                name = ((Member) name).getUser();
            String n = ReflectionUtils.invokeMethodEx(
                    name.getClass(),
                    "getName",
                    name
            );
            if (n == null)
                return "null";
            return n;
        } catch (Exception ex) {
            Skript.warning("Cannot get the discord name of a non-discord entity ("+name.getClass().toString()+")!");
            return "null";
        }
    }

    public static String join(final @Nullable Object[] strings, final String delimiter) {
        if (strings == null)
            return "";
        return join(strings, delimiter, 0, strings.length);
    }

    public static boolean isAnyNull(Object ... objects) {
        for (Object o : objects) {
            if (o == null)
                return true;
        }
        return false;
    }

    public static String join(final @Nullable Object[] strings, final String delimiter, final int start, final int end) {
        if (strings == null)
            return "";
        assert start >= 0 && start <= end && end <= strings.length : start + ", " + end + ", " + strings.length;
        if (start < 0 || start >= strings.length || start == end)
            return "";
        final StringBuilder b = new StringBuilder("" + strings[start]);
        for (int i = start + 1; i < end; i++) {
            b.append(delimiter);
            b.append(strings[i]);
        }
        return "" + b;
    }

    public static String repeat(String str, int amount) {
        return new String(new char[amount]).replace("\0", str);
    }

    public static boolean containInterface(Class<?> clazz, Class<?> inter) {
        return Arrays.asList(clazz.getInterfaces()).contains(inter);
    }

    public static Integer round(Double number) {
        String t = number.toString().split("\\.")[0];
        return Integer.valueOf(t);
    }

    public static void addEmoteToMessage(Emote emote, Message message) {
        if (emote.isAnimated()) {
            message.addReaction(emote.getEmote()).queue();
        } else {
            if (emote.isEmote()) {
                message.addReaction(emote.getEmote()).queue();
            } else {
                message.addReaction(emote.getAsMention()).queue();
            }
        }
    }

    public static boolean areEmojiSimilar(MessageReaction.ReactionEmote first, Emote second) {
        try {
            if (first.isEmote()) {
                Emote f = Emote.fromJDA(first.getEmote());
                return f.getName().equalsIgnoreCase(second.getName());
            } else {
                return first.getEmoji().equalsIgnoreCase(Emojis.ofShortcode(second.getName()).unicode());
            }
        } catch (Exception ex) {
            return false;
        }
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

    public static <T> List<T> convert(Iterator<T> iterator) {
        final List<T> list = new ArrayList<>();
        while (iterator.hasNext())
            list.add(iterator.next());
        return list;
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

    public static Color convert(java.awt.Color original) {
        return new ColorRGB(original.getRed(), original.getGreen(), original.getBlue());
    }

    public static <T> List<T> convert(Collection<T> original) {
        return (List<T>) Arrays.asList(original.toArray(new Object[0]));
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
        Class<? extends Event>[] events = SkriptAdapter.getInstance().getCurrentEvents();
        for (Class<? extends Event> e : events == null ? new Class[0] : events) {
            Getter getter = EventValues.getEventValueGetter(e, clazz, 0);
            if (getter != null) {
                return new SimpleExpression<T>() {
                    @Override
                    protected T @NotNull [] get(@NotNull Event e) {
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
                    public @NotNull Class<? extends T> getReturnType() {
                        return clazz;
                    }

                    @Override
                    public boolean isDefault() {
                        return true;
                    }

                    @Override
                    public @NotNull String toString(Event e, boolean debug) {
                        return "defaulted event value expression";
                    }

                    @Override
                    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
                        return true;
                    }
                };
            }
        }
        if (clazz.equals(Bot.class))
            return (Expression<T>) new SimpleLiteral<>(new Bot(), true);
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

    public static <T> @Nullable Variable<T> parseVar(Expression<T> expression, boolean shouldBeList, boolean showError) {
        if (expression == null)
            return null;
        if (expression instanceof Variable) {
            if (shouldBeList && !((Variable<T>) expression).isList()) {
                if (showError)
                    Skript.error("The specified variable must be a list!");
                return null;
            }
            return (Variable<T>) expression;
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
        List<Button> lastBtnRow = new ArrayList<>();
        for (Object o : vars) {
            if (o instanceof ButtonRow) {
                rows.add(
                        ActionRow.of(((ButtonRow) o).getButtons())
                );
            } else if (o instanceof SelectionMenu.Builder) {
                rows.add(
                        ActionRow.of(((SelectionMenu.Builder) o).build())
                );
            } else {
                lastBtnRow.add((Button) o);
            }
        }
        if (!lastBtnRow.isEmpty())
            rows.add(ActionRow.of(lastBtnRow));
        return rows.toArray(new ActionRow[0]);
    }
}
