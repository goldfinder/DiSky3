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
import info.itsthesky.disky3.core.events.audio.TrackEvent;
import info.itsthesky.disky3.api.Utils;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

@Name("Pause Guild Audio")
@Description("Pause the current audio a guild is playing.")
@Examples("discord command pause:\n" +
        "\tprefixes: *\n" +
        "\ttrigger:\n" +
        "\t\tpause in event-guild")
@Since("1.6")
public class EffPauseAudio extends Effect {

    static {
        Skript.registerEffect(EffPauseAudio.class,
                "pause [the] [audio] [track] in [the] [guild] %guild%");
    }

    private Expression<Guild> exprGuild;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprGuild = (Expression<Guild>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        Guild guild = exprGuild.getSingle(e);
        if (guild == null) return;
        AudioUtils.getGuildAudioPlayer(guild).getPlayer().setPaused(true);
        Utils.sync(() -> Bukkit.getPluginManager().callEvent(new TrackEvent(TrackEvent.TrackState.PAUSE, guild.getJDA(), guild, AudioUtils.getGuildAudioPlayer(guild).getScheduler().getQueue().peek())));
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "pause audio track in guild " + exprGuild.toString(e, debug);
    }

}
