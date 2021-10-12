package info.itsthesky.disky3.core.components;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Min Option")
@Description("Represent the amount of minimum options the user must selected before updating the dropdown. Must be bigger (or equal) to 1!")
public class ExprSelectMin extends SimplePropertyExpression<SelectionMenu.Builder, Number> {
    static {
        register(
                ExprSelectMin.class,
                Number.class,
                "min[imum] (range|selected options)",
                "selectbuilder"
        );
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "minimum selected options";
    }

    @Nullable
    @Override
    public Number convert(SelectionMenu.Builder builder) {
        return builder.getMinValues();
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Number value = (Number) delta[0];
        for (SelectionMenu.Builder builder : getExpr().getAll(e)) {
            builder.setMinValues(value.intValue());
        }
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Number.class);
        }
        return new Class[0];
    }

    @Override
    public @NotNull Class<? extends Number> getReturnType() {
        return Number.class;
    }
}