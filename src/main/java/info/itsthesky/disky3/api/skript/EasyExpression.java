package info.itsthesky.disky3.api.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Easy expression doesn't have any expression in their pattern and cane therefore handled easier.
 * @param <T> The entity type
 */
public abstract class EasyExpression<T> extends SimpleExpression<T> {

    public static <T> void register(Class<? extends EasyExpression<T>> syntax,
                                    Class<T> entity,
                                    String pattern) {
        Skript.registerExpression(
                syntax, entity,
                ExpressionType.SIMPLE, pattern
        );
    }

    protected abstract List<T> convert();

    protected abstract String getSyntax();

    @Override
    @SuppressWarnings("unchecked")
    protected T @NotNull [] get(@NotNull Event e) {
        return isSingle() ?(T[]) convert().toArray(new Object[0])[0] : (T[]) convert().toArray(new Object[0]);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return getSyntax();
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }
}
