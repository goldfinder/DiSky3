package info.itsthesky.disky3.api.skript.adapter;

import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import ch.njol.skript.ScriptLoader;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.log.HandlerList;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.util.Version;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.ReflectionUtils;
import org.bukkit.event.Event;

public class SkriptV2_3 implements SkriptAdapter {

    @Override
    public void setHasDelayedBefore(Kleenean value) {

        ReflectionUtils.setField(
                ScriptLoader.class,
                null,
                "hasDelayBefore",
                value
        );

    }

    @Override
    public Kleenean getHasDelayedBefore() {
        return ReflectionUtils.getFieldValue(
                ScriptLoader.class,
                "hasDelayBefore"
        );
    }

    @Override
    public HandlerList getHandlers() {
        return ReflectionUtils.getFieldValue(SkriptLogger.class, "handlers");
    }

    @Override
    public Class<? extends Event>[] getCurrentEvents() {
        return ScriptLoader.getCurrentEvents();
    }

    @SafeVarargs
    @Override
    public final boolean isCurrentEvents(Class<? extends Event>... events) {
        return ScriptLoader.isCurrentEvent(events);
    }

}
