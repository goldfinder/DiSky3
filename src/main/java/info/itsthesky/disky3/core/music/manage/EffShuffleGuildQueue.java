package info.itsthesky.disky3.core.music.manage;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.music.AudioUtils;
import info.itsthesky.disky3.api.music.GuildAudioManager;
import info.itsthesky.disky3.api.Utils;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@Name("Shuffle Guild Queue")
@Description("Shuffle the current queue of a specific guild.")
@Examples("shuffle queue of event-guild")
@Since("1.7")
public class EffShuffleGuildQueue extends Effect {

    static {
        Skript.registerEffect(EffShuffleGuildQueue.class,
                "shuffle [current] queue (from|of) [the] [guild] %guild%");
    }

    private Expression<Guild> exprGuild;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprGuild = (Expression<Guild>) exprs[0];
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void execute(@NotNull Event e) {
        Guild guild = exprGuild.getSingle(e);
        if (guild == null) return;
        GuildAudioManager manager = AudioUtils.getGuildAudioPlayer(guild);
        manager.trackScheduler.shuffleQueue();
    }

    @Override
    public @NotNull String toString(Event e, boolean debug) {
        return "shuffle current queue of guild " + exprGuild.toString(e, debug);
    }

}
