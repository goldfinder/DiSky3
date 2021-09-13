package info.itsthesky.disky3.core.components;

import ch.njol.skript.classes.Changer;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import info.itsthesky.disky3.api.wrapper.ButtonRow;
import net.dv8tion.jda.api.interactions.components.Button;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffAddButtonToRow extends MultiplyPropertyExpression<ButtonRow, Button> {

    static {
        register(EffAddButtonToRow.class,
                Button.class,
                "button[s]",
                "buttonrow"
        );
    }
    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        Button button = (Button) delta[0];
        switch (mode) {
            case ADD:
                for (ButtonRow row : getExpr().getAll(e)) {
                    row.addButton(button);
                }
                break;
            case SET:
                for (ButtonRow row : getExpr().getAll(e)) {
                    row.clearButtons();
                    row.addButton(button);
                }
                break;
        }
    }

    @Override
    protected String getPropertyName() {
        return "rows";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode.equals(Changer.ChangeMode.ADD) || mode.equals(Changer.ChangeMode.SET))
            return CollectionUtils.array(Button.class);
        return new Class[0];
    }

    @Nullable
    @Override
    public Button[] convert(ButtonRow buttonRow) {
        return buttonRow.getButtons().toArray(new Button[0]);
    }

    @Override
    public Class<? extends Button> getReturnType() {
        return Button.class;
    }
}