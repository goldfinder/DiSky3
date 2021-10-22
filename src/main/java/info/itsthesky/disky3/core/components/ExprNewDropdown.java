package info.itsthesky.disky3.core.components;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.entities.GuildThread;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprNewDropdown extends SimpleExpression<SelectionMenu.Builder> {

    static {
        Skript.registerExpression(
                ExprNewDropdown.class,
                SelectionMenu.Builder.class,
                ExpressionType.SIMPLE,
                "[a] new drop[( |-)]down with [the] id %string%"
        );
    }

    @Override
    protected SelectionMenu.Builder @NotNull [] get(@NotNull Event e) {
        if (exprId.getSingle(e) == null)
            return new SelectionMenu.Builder[0];
        return new SelectionMenu.Builder[] {SelectionMenu.create(exprId.getSingle(e))};
    }

    private Expression<String> exprId;

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends SelectionMenu.Builder> getReturnType() {
        return SelectionMenu.Builder.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "new dropdown with id " + exprId.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprId = (Expression<String>) exprs[0];
        return true;
    }
}
