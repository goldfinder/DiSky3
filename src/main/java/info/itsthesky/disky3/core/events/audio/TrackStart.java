package info.itsthesky.disky3.core.events.audio;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class TrackStart extends SkriptEvent {

    static {
        DiSkyEvent.register("Track Start", TrackStart.class, TrackEvent.class,
                "track start")
                .setName("Track Start");
    }

    private Expression<Bot> bot;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Literal<?>[] exprs, int i, SkriptParser.ParseResult parseResult) {
        bot = (Expression<Bot>) exprs[0];
        return true;
    }

    @Override
    public boolean check(Event event) {
        return ((TrackEvent) event).getState() == TrackEvent.TrackState.START;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "on track start" + (bot == null ? "" : " seen by " + bot.toString(e, debug));
    }

}