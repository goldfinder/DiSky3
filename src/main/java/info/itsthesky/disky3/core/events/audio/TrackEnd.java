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

public class TrackEnd extends SkriptEvent {

    static {
        DiSkyEvent.register("Track End", TrackEnd.class, TrackEvent.class,
                "track end")
                .setName("Track End");

    }

    private Expression<Bot> bot;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Literal<?> @NotNull [] exprs, int i, SkriptParser.@NotNull ParseResult parseResult) {
        bot = (Expression<Bot>) exprs[0];
        return true;
    }

    @Override
    public boolean check(@NotNull Event event) {
        return ((TrackEvent) event).getState() == TrackEvent.TrackState.END;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "on track end" + (bot == null ? "" : " seen by" + bot.toString(e, debug));
    }

}