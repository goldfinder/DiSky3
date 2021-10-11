package info.itsthesky.disky3.core.skript.markdown;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.table.TableRow;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NewTable extends SimpleExpression<Table.Builder> {

    static {
        Skript.registerExpression(
                NewTable.class,
                Table.Builder.class,
                ExpressionType.SIMPLE,
                "[a] [new] [mark[( |-)]down] table"
        );
    }

    @Override
    protected Table.Builder @NotNull [] get(@NotNull Event e) {
        return new Table.Builder[] {new Table.Builder()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Table.Builder> getReturnType() {
        return Table.Builder.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "a new mark down table";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }
}
