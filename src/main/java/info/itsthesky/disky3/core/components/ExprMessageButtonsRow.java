package info.itsthesky.disky3.core.components;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import info.itsthesky.disky3.api.wrapper.ButtonRow;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ExprMessageButtonsRow extends MultiplyPropertyExpression<UpdatingMessage, Object> {
    static {
        register(
                ExprMessageButtonsRow.class,
                Object.class,
                "(row[s]|component[s])",
                "message"
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (parseResult.expr.contains("inventory"))
            return false;
        this.expr = (Expression<UpdatingMessage>) expr[0];
        return true;
    }

    @Override
    protected String getPropertyName() {
        return "components";
    }

    @Nullable
    @Override
    public Object[] convert(UpdatingMessage updatingMessage) {
        return updatingMessage
                .getMessage()
                .getActionRows()
                .stream()
                .map(Object::toString)
                .toArray();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        UpdatingMessage message = getExpr().getSingle(e);
        if (message == null) return;
        List<ActionRow> rows = new ArrayList<>();
        switch (mode) {
            case SET:
                rows = new ArrayList<>();
                break;
            case ADD:
                rows = new ArrayList<>(message.getMessage().getActionRows());
                break;
            case RESET:
            case REMOVE_ALL:
                message
                        .getMessage()
                        .editMessage(message.getMessage())
                        .setActionRows()
                        .queue();
                return;
        }
        for (Object component : delta) {
            if (component instanceof ButtonRow) {
                List<Button> buttons = ((ButtonRow) component).getButtons();
                if (buttons.size() > 0) rows.add(ActionRow.of(buttons.toArray(new Component[0])));
            } else {
                SelectionMenu selects = ((SelectionMenu.Builder) component).build();
                rows.add(ActionRow.of(selects));
            }
        }
        message
                .getMessage()
                .editMessage(message.getMessage())
                .setActionRows(rows.toArray(new ActionRow[0]))
                .queue();
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        switch (mode) {
            case SET:
            case ADD:
            case RESET:
            case REMOVE_ALL:
                return CollectionUtils.array(ButtonRow[].class, SelectionMenu.Builder[].class);
            default:
                return new Class[0];
        }
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }
}