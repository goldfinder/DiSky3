package info.itsthesky.disky3.api.skript.events;

import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Made by Blitz, minor edit by Sky for DiSky
 */
public class EventListener<T> extends ListenerAdapter {

    public final static ArrayList<EventListener<?>> listeners = new ArrayList<>();
    public boolean enabled = true;
    private final Class<T> clazz;
    private final Consumer<T> consumer;

    public EventListener(Class<T> paramClass, Consumer<T> consumer) {
        this.clazz = paramClass;
        this.consumer = consumer;
    }

    public static void addListener(EventListener<?> listener) {
        removeListener(listener);
        listeners.add(listener);
        BotManager.globalExecute(bot -> bot.getCore().addEventListener(listener));
    }

    public static void removeListener(EventListener<?> listener) {
        listeners.remove(listener);
        BotManager.globalExecute(bot -> bot.getCore().removeEventListener(listener));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onGenericEvent(GenericEvent event) {
        if (enabled && clazz.isInstance(event)) {
            consumer.accept((T) event);
        }
    }

}