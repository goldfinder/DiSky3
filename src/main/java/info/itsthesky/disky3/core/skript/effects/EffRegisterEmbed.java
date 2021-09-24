package info.itsthesky.disky3.core.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.EmbedManager;
import info.itsthesky.disky3.api.skript.AsyncEffect;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public class EffRegisterEmbed extends AsyncEffect {

    static {
        Skript.registerEffect(EffRegisterEmbed.class,
                "register [new] [embed] template with [the] [embed] %embedbuilder% [and] with [the] (name|id) %string%");
    }

    private Expression<String> exprID;
    private Expression<EmbedBuilder> exprEmbed;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprEmbed = (Expression<EmbedBuilder>) exprs[0];
        exprID = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(@NotNull Event e) {
        String id = exprID.getSingle(e);
        EmbedBuilder builder = exprEmbed.getSingle(e);
        if (id == null || builder == null) return;
        EmbedManager.registerTemplate(id, builder);
    }

    @Override
    public @NotNull String toString(Event e, boolean debug) {
        return "register new embed template from " + exprEmbed.toString(e, debug) + " with id " + exprID.toString(e, debug);
    }

}