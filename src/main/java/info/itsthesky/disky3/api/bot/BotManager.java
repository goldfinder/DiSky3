package info.itsthesky.disky3.api.bot;

import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.messages.MessageUpdater;
import info.itsthesky.disky3.api.skript.events.EventListener;
import info.itsthesky.disky3.api.wrapper.InviteTracking;
import info.itsthesky.disky3.core.commands.CommandListener;
import info.itsthesky.disky3.core.skript.slashcommand.SlashListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class BotManager {

    private static final List<Bot> LOADED_BOTS = new ArrayList<>();

    public static List<Bot> getLoadedBots() {
        if (LOADED_BOTS.isEmpty()) {
            DiSky.exception(new DiSkyException("You are trying to get a bot, but none of them are loaded!"));
            return Collections.singletonList(new Bot());
        } else {
            return LOADED_BOTS;
        }
    }

    public static void reset() {
        LOADED_BOTS.forEach(bot -> bot.getCore().shutdownNow());
        LOADED_BOTS.clear();
    }

    public static boolean isLoaded(String name) {
        return LOADED_BOTS
                .stream()
                .anyMatch(loop -> name.equalsIgnoreCase(loop.getName()));
    }

    public static Set<JDA> getBotsJDA() {
        return getLoadedBots().stream().map(Bot::getCore).collect(Collectors.toSet());
    }

    public static boolean add(Bot bot) {
        if (isLoaded(bot.getName()))
            return false;
        bot.getCore().addEventListener(new CommandListener());
        bot.getCore().addEventListener(new MessageUpdater());
        bot.getCore().addEventListener(new SlashListener());
        bot.getCore().addEventListener(new InviteTracking());
        bot.getCore().addEventListener((Object[]) EventListener.listeners.toArray(new ListenerAdapter[0]));
        LOADED_BOTS.add(bot);
        bot.getCore().updateCommands().queue();
        return true;
    }

    @Nullable
    public static Bot searchFromName(String name) {
        final Optional<Bot> op = LOADED_BOTS
                .stream()
                .filter(bot -> bot.getName().equalsIgnoreCase(name))
                .findAny();
        return op.orElse(null);
    }

    @Nullable
    public static Bot searchFromJDA(JDA core) {
        final Optional<Bot> op = LOADED_BOTS
                .stream()
                .filter(bot -> bot.getCore().equals(core))
                .findAny();
        return op.orElse(null);
    }

    public static void globalExecute(Consumer<Bot> consumer) {
        LOADED_BOTS.forEach(consumer);
    }

    @Nullable
    public static <T> T globalSearch(Function<Bot, T> function) {
        T value = null;
        for (Bot bot : LOADED_BOTS) {
            if (function.apply(bot) != null)
                value = function.apply(bot);
        }
        return value;
    }

    @Nullable
    public static <T> T specificSearch(Bot bot, Function<Bot, T> function) {
        return function.apply(bot);
    }

    public static boolean remove(Bot bot) {
        if (!isLoaded(bot.getName()))
            return false;
        LOADED_BOTS.remove(bot);
        DiSky.warn("Shutdown of bot " + bot.getName() + " ...");
        bot.getCore().shutdownNow();
        DiSky.success("Shutdown complete!");
        return true;
    }
}
