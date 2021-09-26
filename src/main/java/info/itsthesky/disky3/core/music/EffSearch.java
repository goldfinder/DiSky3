package info.itsthesky.disky3.core.music;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.music.AudioSite;
import info.itsthesky.disky3.api.music.AudioUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public class EffSearch extends Effect {

    static {
        Skript.registerEffect(EffSearch.class, "search [in] [[web]site] %audiosite% for [the] [input] %string% and store (it|the results) in %objects%");
    }

    private Expression<AudioSite> exprSite;
    private Expression<String> exprQueries;
    private Variable<?> var;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprSite = (Expression<AudioSite>) exprs[0];
        exprQueries = (Expression<String>) exprs[1];
        if (Utils.parseVar(exprs[2], true) == null) return false;
        var = Utils.parseVar(exprs[2], true);
        return true;
    }

    @Override
    public void execute(@NotNull Event e) {
        AudioSite site = exprSite.getSingle(e);
        if (site == null) return;
        String query = exprQueries.getSingle(e);
        AudioTrack[] results = AudioUtils.search(query, site);
        if (var != null)
            var.change(e, results, Changer.ChangeMode.SET);
    }

    @Override
    public @NotNull String toString(Event event, boolean debug) {
        return "search " + exprSite.toString(event, debug) + " for " + exprQueries.toString(event, debug) + " and store the results in " + var.toString(event, debug);
    }

}