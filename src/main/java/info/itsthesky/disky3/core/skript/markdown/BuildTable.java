package info.itsthesky.disky3.core.skript.markdown;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import net.steppschuh.markdowngenerator.table.Table;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BuildTable extends SimpleExpression<String> {

    static {
        Skript.registerExpression(
                BuildTable.class,
                String.class,
                ExpressionType.SIMPLE,
                "built %tables%"
        );
    }

    private Expression<Table.Builder> exprTables;

    @Override
    protected @Nullable
    String @NotNull [] get(@NotNull Event e) {
        return Arrays
                .stream(Utils.verifyVars(e, exprTables, new Table.Builder[0]))
                .map(table -> table.build().toString())
                .toArray(String[]::new);
    }

    @Override
    public boolean isSingle() {
        return exprTables.isSingle();
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "build " + exprTables.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprTables = (Expression<Table.Builder>) exprs[0];
        return true;
    }
}
