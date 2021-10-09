package info.itsthesky.disky3.core.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.section.EffectSection;
import info.itsthesky.disky3.api.section.RestExceptionSection;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprException extends SimpleExpression<String> {

    static {
        Skript.registerExpression(
                ExprException.class,
                String.class,
                ExpressionType.SIMPLE,
                "[the] [last] [discord] (error|exception)"
        );
    }

    public static Throwable lastException;

    @Override
    protected String @NotNull [] get(@NotNull Event e) {
        return new String[] {lastException.getMessage()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the last exception";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }
}
