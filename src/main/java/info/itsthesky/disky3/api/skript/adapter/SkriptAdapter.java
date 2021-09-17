package info.itsthesky.disky3.api.skript.adapter;

import ch.njol.skript.log.HandlerList;
import ch.njol.skript.util.Version;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import org.bukkit.event.Event;

/**
 * Thanks to anarchick for the versioning check!
 * @author ItsTheSky / Anarchick
 */
public interface SkriptAdapter {

    static SkriptAdapter getInstance() {
        return DiSky.getSkriptAdapter();
    }

    Version getMinimalVersion();

    void setHasDelayedBefore(Kleenean value);

    Kleenean getHasDelayedBefore();

    HandlerList getHandlers();

    boolean isCurrentEvents(Class<? extends Event>... events);

}
