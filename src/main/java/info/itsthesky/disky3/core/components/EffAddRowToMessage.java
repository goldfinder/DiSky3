package info.itsthesky.disky3.core.components;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.wrapper.ButtonRow;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EffAddRowToMessage extends Effect {

    static {
        Skript.registerEffect(EffAddRowToMessage.class,
                "add [the] [button] row[s] %buttonrows% to [the] [message] %message%",
                "set [the] [button] row[s] of [the] [message] %message% to %buttonrows%"
        );
    }

    private Expression<ButtonRow> exprRow;
    private Expression<UpdatingMessage> exprMessage;
    private boolean isSet;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprRow = (Expression<ButtonRow>) (matchedPattern == 0 ? exprs[0] : exprs[1]);
        exprMessage = (Expression<UpdatingMessage>) (matchedPattern == 0 ? exprs[1] : exprs[0]);
        isSet = matchedPattern == 1;
        return true;
    }

    @Override
    protected void execute(@NotNull Event e) {
        UpdatingMessage message = exprMessage.getSingle(e);
        ButtonRow[] row1 = exprRow.getAll(e);
        if (message == null || row1.length == 0) return;

        List<ActionRow> rows = new ArrayList<>(message.getMessage().getActionRows());
        if (!isSet) {
            for (ButtonRow row : row1) {
                List<Button> buttons = row.getButtons();
                if (buttons.size() > 0) rows.add(ActionRow.of(buttons.toArray(new Component[0])));
            }
            message.getMessage().editMessage(message.getMessage())
                    .setActionRows(rows)
                    .queue();
        } else {
            rows = new ArrayList<>();
            for (ButtonRow row : row1) {
                List<Button> buttons = row.getButtons();
                if (buttons.size() > 0) rows.add(ActionRow.of(buttons.toArray(new Component[0])));
            }
            message.getMessage()
                    .editMessage(message.getMessage())
                    .setActionRows(rows)
                    .queue();
        }
    }

    @Override
    public @NotNull String toString(Event e, boolean debug) {
        return "add the row " + exprRow.toString(e, debug) + " to the message " + exprMessage.toString(e, debug);
    }

}
