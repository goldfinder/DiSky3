package info.itsthesky.disky3.core.components;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.wrapper.ButtonRow;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public class ExprNewButtonsRow extends SimpleExpression<ButtonRow> {
    
    static {
        Skript.registerExpression(ExprNewButtonsRow.class, ButtonRow.class, ExpressionType.SIMPLE,
                "[a] new [buttons] row");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    protected ButtonRow @NotNull [] get(@NotNull Event e) {
        return new ButtonRow[] {new ButtonRow()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends ButtonRow> getReturnType() {
        return ButtonRow.class;
    }

    @Override
    public @NotNull String toString(Event e, boolean debug) {
        return "new button row";
    }
}