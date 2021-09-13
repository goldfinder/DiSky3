package info.itsthesky.disky3.api.oldcomponents;

import ch.njol.skript.classes.Changer;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MessageRows extends MultiplyPropertyExpression<UpdatingMessage, ComponentRow> {

    static {
        register(
                MessageRows.class,
                ComponentRow.class,
                "[discord] [message] (component|row)[s]",
                "message"
        );
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {

        switch (mode) {
            case SET:
            case ADD:
                return CollectionUtils.array(ComponentRow.class, SelectionMenu.class);
            default:
                return CollectionUtils.array();
        }

    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {

        if (mode == Changer.ChangeMode.REMOVE_ALL) {
            UpdatingMessage message = Utils.verifyVar(e, getExpr(), null);
            message.getMessage().editMessage(message.getMessage()).setActionRows(new ActionRow[0]).queue();
            return;
        }

        if (delta == null || delta.length == 0 || delta[0] == null) return;
        UpdatingMessage message = Utils.verifyVar(e, getExpr(), null);
        List<ActionRow> input = ComponentRow.convert((ComponentRow[]) delta);
        if (message == null) return;

        List<ActionRow> current = message.getMessage().getActionRows();

        switch (mode) {

            case ADD:
                current.addAll(input);
                break;
            case SET:
                current = input;
                break;
            /* case REMOVE_ALL:
                current = new ArrayList<>();
                break; */

        }

        message
                .getMessage()
                .editMessage(message.getMessage())
                .setActionRows(current)
                .queue();

    }

    @Override
    protected @NotNull String getPropertyName() {
        return "component rows";
    }

    @Override
    public @Nullable ComponentRow[] convert(@NotNull UpdatingMessage message) {
        return ComponentRow.unConvert(message.getMessage().getActionRows()).toArray(new ComponentRow[0]);
    }

    @Override
    public @NotNull Class<? extends ComponentRow> getReturnType() {
        return ComponentRow.class;
    }
}
