package info.itsthesky.disky3.core.components;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.skript.NodeInformation;
import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import info.itsthesky.disky3.core.events.component.ButtonClick;
import info.itsthesky.disky3.core.events.component.SelectionMenu;
import jdk.jfr.Name;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Event Choices")
@Description("Represent the choices / options the user selected in a dropdown update event. They are a list of option ID (String), not a single item!")
public class EventChoices extends SimpleExpression<String> {

    static {
        Skript.registerExpression(
                EventChoices.class,
                String.class,
                ExpressionType.SIMPLE,
                "event-(options|choices)"
        );
    }

    @Override
    protected String @NotNull [] get(@NotNull Event e) {
        SelectionMenu.EvtSelectionMenu event = (SelectionMenu.EvtSelectionMenu) e;
        return event.getJDAEvent().getValues().toArray(new String[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "event-choices";
    }

    private NodeInformation node;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        node = new NodeInformation();

        if (!SkriptAdapter.getInstance().isCurrentEvents(
                SelectionMenu.EvtSelectionMenu.class
        )) {
            Skript.error("The event-choices expression can only be used in a dropdown click event!");
            return false;
        }

        return true;
    }
}
