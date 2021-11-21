package info.itsthesky.disky3.core.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Last Exception")
@Description({"The message of the last exception occurred in an exception section.",
"Some effect, like ban, unban, etc... can be converted into section to catch error, and make a more user-friendly answer."})
@Examples("the last disky exception")
public class ExprException extends SimpleExpression<String> {

    static {
        Skript.registerExpression(
                ExprException.class,
                String.class,
                ExpressionType.SIMPLE,
                "[the] [last] [(discord|disky)] (error|exception)"
        );
    }

    public static Throwable lastException;

    @Override
    protected String @NotNull [] get(@NotNull Event e) {
        return lastException == null ? new String[0] : new String[] {lastException.getMessage()};
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
