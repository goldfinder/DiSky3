package info.itsthesky.disky3.api.bot;

import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.cache.MessagesManager;
import info.itsthesky.disky3.api.messages.MessageUpdater;
import info.itsthesky.disky3.api.skript.events.EventListener;
import info.itsthesky.disky3.api.wrapper.InviteTracking;
import info.itsthesky.disky3.core.commands.CommandListener;
import info.itsthesky.disky3.core.skript.slashcommand.SlashFactory;
import info.itsthesky.disky3.core.skript.slashcommand.SlashListener;
import info.itsthesky.disky3.core.skript.slashcommand.SlashObject;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
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
            return Collections.singletonList(new Bot());
        } else {
            return LOADED_BOTS;
        }
    }

    public static boolean anyBotLoaded() {
        return !LOADED_BOTS.isEmpty();
    }

    public static void registerEvent(ListenerAdapter event) {
        globalExecute(bot -> bot.getCore().addEventListener(event));
    }

    public static void removeEvent(ListenerAdapter event) {
        globalExecute(bot -> bot.getCore().removeEventListener(event));
    }

    public static void reset() {
        getLoadedBots().forEach(bot -> bot.getCore().shutdownNow());
        getLoadedBots().clear();
    }

    public static boolean isLoaded(String name) {
        return getLoadedBots()
                .stream()
                .anyMatch(loop -> loop != null && name != null && name.equalsIgnoreCase(loop.getName()));
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
        bot.getCore().addEventListener(new MessagesManager());
        bot.getCore().addEventListener(new SlashFactory.SlashQueueListener());
        bot.getCore().addEventListener((Object[]) EventListener.listeners.toArray(new ListenerAdapter[0]));
        LOADED_BOTS.add(bot);
        try {
            bot.getCore().updateCommands().queue();
        } catch (Exception ignored) {}
        return true;
    }

    @Nullable
    public static Bot searchFromName(String name) {
        final Optional<Bot> op = getLoadedBots()
                .stream()
                .filter(bot -> bot.getName().equalsIgnoreCase(name))
                .findAny();
        return op.orElse(null);
    }

    @Nullable
    public static Bot searchFromJDA(JDA core) {
        final Optional<Bot> op = getLoadedBots()
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
        for (Bot bot : getLoadedBots()) {
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
        try {
            bot.getCore().updateCommands().complete();
            for (Guild guild : bot.getCore().getGuilds()) {
                guild.updateCommands().complete();
            }
        } catch (Exception ignored) {}
        getLoadedBots().remove(bot);
        DiSky.warn("Shutdown of bot " + bot.getName() + " ...");
        bot.getCore().shutdownNow();
        DiSky.success("Shutdown complete!");
        return true;
    }
}
