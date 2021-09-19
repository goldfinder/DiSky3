package info.itsthesky.disky3.core.skript.properties.embed;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public class EffAddField extends Effect {

    static {
        Skript.registerEffect(EffAddField.class,
                "add field (named|with name) %string% [and] with [the] value %string% to [fields of] %embedbuilder%",
                "add inline field (named|with name) %string% [and] with [the] value %string% to [fields of] %embedbuilder%");
    }

    private Expression<String> exprName;
    private Expression<String> exprDesc;
    private Expression<EmbedBuilder> exprEmbed;
    private boolean isInline;
    private NodeInformation node;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprName = (Expression<String>) exprs[0];
        exprDesc = (Expression<String>) exprs[1];
        exprEmbed = (Expression<EmbedBuilder>) exprs[2];
        isInline = matchedPattern != 0;
        node = new NodeInformation();
        return true;
    }

    @Override
    protected void execute(@NotNull Event e) {
        String name = exprName.getSingle(e);
        String desc = exprDesc.getSingle(e);
        EmbedBuilder embed = exprEmbed.getSingle(e);
        if (name == null || desc == null || embed == null) return;

        if (name.length() > 256) {
            DiSky.exception(new DiSkyException("The title of a field cannot be bigger than 256 characters. The one you're trying to set is '"+name.length()+"' length!"), node);
            return;
        }
        if (desc.length() > 1024) {
            DiSky.exception(new DiSkyException("The value of a field cannot be bigger than 1024 characters. The one you're trying to set is '"+name.length()+"' length!"), node);
            return;
        }

        embed.addField(name, desc, isInline);
    }

    @Override
    public @NotNull String toString(Event e, boolean debug) {
        return "add field with named " + exprName.toString(e, debug) + " with description " + exprDesc.toString(e, debug) + " to fields of " + exprEmbed.toString(e, debug);
    }

}