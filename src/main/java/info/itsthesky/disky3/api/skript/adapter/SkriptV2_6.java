package info.itsthesky.disky3.api.skript.adapter;

import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.log.HandlerList;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.util.Version;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.ReflectionUtils;
import org.bukkit.event.Event;

public class SkriptV2_6 implements SkriptAdapter {

    @Override
    public void setHasDelayedBefore(Kleenean value) {
        ParserInstance.get().setHasDelayBefore(value);
    }

    @Override
    public Kleenean getHasDelayedBefore() {
        return ParserInstance.get().getHasDelayBefore();
    }

    @Override
    public HandlerList getHandlers() {
        return ParserInstance.get().getHandlers();
    }

    @Override
    public Class<? extends Event>[] getCurrentEvents() {
        return ParserInstance.get().getCurrentEvents();
    }

    @SafeVarargs
    @Override
    public final boolean isCurrentEvents(Class<? extends Event>... events) {
        return ParserInstance.get().isCurrentEvent(events);
    }

    @Override
    public void setCurrentEvent(String name, Class<? extends Event>... events) {
        ParserInstance.get().setCurrentEvent(name, events);
    }
}
