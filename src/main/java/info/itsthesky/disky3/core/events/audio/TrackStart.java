package info.itsthesky.disky3.core.events.audio;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TrackStart extends BotTrackEvent {

    static {
        DiSkyEvent.register("Track Start", TrackStart.class, TrackEvent.class,
                "track start [seen by %-string%]")
                .setName("Track Start")
                .setDesc("Fired when a track starts playing.")
                .setExample("on track start:");
    }

    private Expression<Bot> bot;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Literal<?> @NotNull [] exprs, int i, SkriptParser.@NotNull ParseResult parseResult) {
        bot = (Expression<Bot>) exprs[0];
        return super.init(exprs, i, parseResult);
    }

    @Override
    public boolean check(@NotNull Event event) {
        return ((TrackEvent) event).getState() == TrackEvent.TrackState.START;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "on track start" + (bot == null ? "" : " seen by " + bot.toString(e, debug));
    }

}