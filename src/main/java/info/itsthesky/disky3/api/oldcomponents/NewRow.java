package info.itsthesky.disky3.api.oldcomponents;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NewRow extends SimpleExpression<ComponentRow> {

    static {
        Skript.registerExpression(
                NewRow.class,
                ComponentRow.class,
                ExpressionType.SIMPLE,
                "[a] new [discord] [(component|interaction)[s]] row"
        );
    }

    @Override
    protected ComponentRow @NotNull [] get(@NotNull Event e) {
        return new ComponentRow[] {new ComponentRow()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends ComponentRow> getReturnType() {
        return ComponentRow.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "new components row";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }

}
