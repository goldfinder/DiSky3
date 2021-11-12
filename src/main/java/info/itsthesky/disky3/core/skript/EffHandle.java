package info.itsthesky.disky3.core.skript;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.section.RestExceptionSection;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EffHandle extends RestExceptionSection {

    static {
        register(
                EffHandle.class,
                "<.+> and (handle|catch) exception"
        );
    }

    private Effect effect;

    @Override
    public boolean init(@NotNull Expression @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        final String effectStr = parseResult.regexes.get(0).group();
        effect = Effect.parse(effectStr, "Can't understand this effect: ");
        return effect != null && super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public RestAction runRestAction(Event e) {
        effect.run(e);
        return null;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return effect.toString(e, debug) + " and handle exception";
    }
}
