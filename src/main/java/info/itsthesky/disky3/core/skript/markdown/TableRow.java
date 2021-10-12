package info.itsthesky.disky3.core.skript.markdown;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import net.steppschuh.markdowngenerator.table.Table;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Insert Row")
@Description("Add a row to a specific table builder. If this is the first effect that the table is used in, then the amount of String chosen will define the base columns.")
public class TableRow extends Effect {

    static {
        Skript.registerEffect(
                TableRow.class,
                "(add|insert) [a] [new] row %strings% to [the] [table] %table%"
        );
    }

    private Expression<Table.Builder> exprTable;
    private Expression<String> exprRows;

    @Override
    protected void execute(@NotNull Event e) {
        final Table.Builder builder = exprTable.getSingle(e);
        final String[] rows = Utils.verifyVars(e, exprRows, new String[0]);
        if (builder == null || rows.length == 0)
            return;
        builder.addRow((Object[]) rows);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "add rows " + exprRows.toString(e, debug) + " to table " + exprTable.toString(e, debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprRows = (Expression<String>) exprs[0];
        exprTable = (Expression<Table.Builder>) exprs[1];
        return true;
    }
}
