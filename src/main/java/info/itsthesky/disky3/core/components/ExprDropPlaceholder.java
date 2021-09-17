package info.itsthesky.disky3.core.components;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class ExprDropPlaceholder extends SimplePropertyExpression<SelectionMenu.Builder, String> {
    static {
        register(
                ExprDropPlaceholder.class,
                String.class,
                "(text|desc[ription]|place[( |-)]holder)",
                "selectbuilder"
        );
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "placeholder";
    }

    @Nullable
    @Override
    public String convert(SelectionMenu.Builder builder) {
        return builder.getPlaceholder();
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        String value = (String) delta[0];
        for (SelectionMenu.Builder builder : getExpr().getAll(e)) {
            builder.setPlaceholder(value);
        }
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(String.class);
        }
        return new Class[0];
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }
}