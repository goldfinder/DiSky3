package info.itsthesky.disky3.api.skript.adapter;

import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.util.Version;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class SkriptV2_6 implements SkriptAdapter {

    @Override
    public Version getMinimalVersion() {
        return new Version(2, 6);
    }

    @Override
    public void setHasDelayedBefore(Kleenean value) {
        ParserInstance.get().setHasDelayBefore(value);
    }

    @Override
    public Kleenean getHasDelayedBefore() {
        return ParserInstance.get().getHasDelayBefore();
    }

    @SafeVarargs
    @Override
    public final boolean isCurrentEvents(Class<? extends Event>... events) {
        return ParserInstance.get().isCurrentEvent(events);
    }
}
