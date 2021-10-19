package info.itsthesky.disky3.core.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprSectionValue extends SimpleExpression<Object> {

    static {
        //Skript.registerExpression(ExprSectionValue.class, Object.class, ExpressionType.PROPERTY, "[the] sec[tion] event-%*classinfo%");// property so that it is parsed after most other expressions
    }

    private ClassInfo<?> info;
    private Expression<?> expr;
    private EventValueExpression<?> value;

    @Override
    protected Object @NotNull [] get(@NotNull Event e) {
        return value.getAll(e);
    }

    @Override
    public boolean isSingle() {
        return expr.isSingle();
    }

    @Override
    public @NotNull Class<?> getReturnType() {
        return expr.getReturnType();
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "section event-" + info.getCodeName();
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        expr = exprs[0];
        info = ((Literal<ClassInfo<?>>) exprs[0]).getSingle();
        value = new EventValueExpression<Object>(info.getC());
        return info != null && value.init();
    }
}
