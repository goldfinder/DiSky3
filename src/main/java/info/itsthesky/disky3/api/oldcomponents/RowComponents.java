package info.itsthesky.disky3.api.oldcomponents;

import ch.njol.skript.classes.Changer;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import net.dv8tion.jda.api.interactions.components.Component;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class RowComponents extends MultiplyPropertyExpression<ComponentRow, Component> {

    static {
        register(
                RowComponents.class,
                Component.class,
                "[discord] [row] (component|button)[s]",
                "message"
        );
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        switch (mode) {
            case SET:
            case ADD:
                return CollectionUtils.array(Component.class);
            default:
                return CollectionUtils.array();
        }
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {

        System.out.println(getExpr().getSingle(e));

        if (delta == null || delta.length == 0 || delta[0] == null) return;
        ComponentRow row = Utils.verifyVar(e, getExpr(), null);
        List<Component> input = Arrays.asList(((Component[]) delta).clone());
        if (row == null)
            return;

        switch (mode) {
            case ADD:
                row.addComponents(input);
                break;
            case SET:
                row.clear();
                row.addComponents(input);
                break;
        }

    }

    @Override
    protected @NotNull String getPropertyName() {
        return "row components";
    }

    @Override
    public @Nullable Component[] convert(ComponentRow components) {
        if (components == null)
            return new Component[0];
        return components.getComponents().toArray(new Component[0]);
    }

    @Override
    public @NotNull Class<? extends Component> getReturnType() {
        return Component.class;
    }

}
