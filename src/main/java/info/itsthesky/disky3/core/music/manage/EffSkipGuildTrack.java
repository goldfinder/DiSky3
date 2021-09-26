package info.itsthesky.disky3.core.music.manage;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky3.api.music.AudioUtils;
import info.itsthesky.disky3.api.Utils;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;

@Name("Skip Guild Track")
@Description("Skip the track from the specific guild.")
@Examples("skip current track of event-guild")
@Since("1.6-pre2, 1.8 (added store pattern)")
public class EffSkipGuildTrack extends Effect {

    static {
        Skript.registerEffect(EffSkipGuildTrack.class, // [the] [bot] [(named|with name)] %string%
                "skip [current] track (from|of) [the] [guild] %guild% [and store (it|the new track) in %-object%]");
    }

    private Expression<Guild> exprGuild;
    private Expression<Object> exprVar;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprGuild = (Expression<Guild>) exprs[0];
        if (exprs.length == 2) exprVar = (Expression<Object>) exprs[1];
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void execute(Event e) {
        Guild guild = exprGuild.getSingle(e);
        if (guild == null) return;
        AudioTrack track = AudioUtils.skipTrack(guild);
        if (exprVar == null) return;
        if (exprVar instanceof Variable) {
            exprVar.change(e, new AudioTrack[] {track}, Changer.ChangeMode.SET);
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "skip current track of guild " + exprGuild.toString(e, debug);
    }

}
