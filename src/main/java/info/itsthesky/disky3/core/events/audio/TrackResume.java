package info.itsthesky.disky3.core.events.audio;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;import info.itsthesky.disky3.api.bot.BotManager;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TrackResume extends SkriptEvent {

    static {
        DiSkyEvent.register("Track Resume", TrackResume.class, TrackEvent.class,
                "track (play|resume)")
                .setName("Track Resume")
                .setDesc("Fired when a track is resumed.")
                .setExample("on track resume:");

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
        return ((TrackEvent) event).getState() == TrackEvent.TrackState.PLAY;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "on track resume" + (bot == null ? "" : " seen by" + bot.toString(e, debug));
    }

}