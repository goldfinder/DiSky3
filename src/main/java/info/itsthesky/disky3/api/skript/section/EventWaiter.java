package info.itsthesky.disky3.api.skript.section;

import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EventWaiter<T extends GenericEvent> extends ListenerAdapter {

    private static final HashMap<String, EventWaiter<?>> REGISTERED_WAITERS = new HashMap<>();

    private final String customID;
    private final Class<T> clazz;
    private final Consumer<T> executor;
    private final Predicate<T> validator;
    private boolean isOneTime = false;

    public EventWaiter(Class<T> clazz, Consumer<T> executor, Predicate<T> validator, String customID) {
        this.clazz = clazz;
        this.executor = executor;
        this.validator = validator;
        this.customID = customID;
        if (REGISTERED_WAITERS.containsKey(customID))
            unregister(REGISTERED_WAITERS.get(customID));
        REGISTERED_WAITERS.put(customID, this);
        BotManager.registerEvent(this);
    }

    public boolean isOneTime() {
        return isOneTime;
    }

    public void setOneTime(boolean oneTime) {
        isOneTime = oneTime;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public Predicate<T> getValidator() {
        return validator;
    }

    public Consumer<T> getExecutor() {
        return executor;
    }

    public String getCustomID() {
        return customID;
    }

    public void unregister() {
        unregister(this);
    }

    public static void unregister(EventWaiter<?> waiter) {
        BotManager.removeEvent(waiter);
        REGISTERED_WAITERS.remove(waiter.getCustomID());
    }

    private boolean alreadyExecuted = false;
    @Override
    public void onGenericEvent(@NotNull GenericEvent event) {
        if (event.getClass().equals(getClazz()) && getValidator().test((T) event))
        {
            if (isOneTime && alreadyExecuted)
            {
                unregister();
                return;
            }
            alreadyExecuted = true;
            getExecutor().accept((T) event);
        }
    }
}
