package info.itsthesky.disky3.api.skript.adapter;

import ch.njol.skript.config.Config;
import ch.njol.skript.log.HandlerList;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import org.bukkit.event.Event;

/**
 * Thanks to Anarchick for the versioning check!
 * @author ItsTheSky / Anarchick
 */
public interface SkriptAdapter {

    static SkriptAdapter getInstance() {
        return DiSky.getSkriptAdapter();
    }

    void setCurrentScript(Config script);

    void setHasDelayedBefore(Kleenean value);

    Kleenean getHasDelayedBefore();

    HandlerList getHandlers();

    Class<? extends Event>[] getCurrentEvents();

    boolean isCurrentEvents(Class<? extends Event>... events);

    void setCurrentEvent(String name, Class<? extends Event>... events);

}
