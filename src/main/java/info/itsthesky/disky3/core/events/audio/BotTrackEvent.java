package info.itsthesky.disky3.core.events.audio;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import info.itsthesky.disky3.api.bot.BotManager;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class BotTrackEvent extends SkriptEvent {

    private String bot;

    @Override
    public boolean init(Literal<?> @NotNull [] exprs, int matchedPattern, SkriptParser.@NotNull ParseResult parseResult) {
        bot = (String) (exprs[0] == null ? null : exprs[0].getSingle());
        return true;
    }

    protected abstract TrackEvent.TrackState validState();

    @Override
    public boolean check(@NotNull Event e) {
        return ((TrackEvent) e).getState().equals(validState()) && bot.equalsIgnoreCase(BotManager.searchFromJDA(((TrackEvent) e).getJDA()).getName());
    }

}
