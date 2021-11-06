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

public class TrackResume extends BotTrackEvent {

    static {
        DiSkyEvent.register("Track Resume", TrackResume.class, TrackEvent.class,
                "track (play|resume) [seen by %-string%]")
                .setName("Track Resume")
                .setDesc("Fired when a track is resumed.")
                .setExample("on track resume:");

    }

    @Override
    protected TrackEvent.TrackState validState() {
        return TrackEvent.TrackState.PLAY;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "on track resume";
    }

}