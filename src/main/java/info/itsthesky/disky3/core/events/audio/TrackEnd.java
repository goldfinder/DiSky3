package info.itsthesky.disky3.core.events.audio;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TrackEnd extends BotTrackEvent {

    static {
        DiSkyEvent.register("Track End", TrackEnd.class, TrackEvent.class,
                "track end [seen by %-string%]")
                .setName("Track End")
                .setDesc("Fired when a track ends.")
                .setExample("on track end:");

    }

    @Override
    protected TrackEvent.TrackState validState() {
        return TrackEvent.TrackState.END;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "on track end";
    }

}