package info.itsthesky.disky3.core.components;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.skript.NodeInformation;
import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import info.itsthesky.disky3.core.events.component.ButtonClick;
import info.itsthesky.disky3.core.events.component.SelectionMenu;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventComponent extends SimpleExpression<String> {

    static {
        Skript.registerExpression(
                EventComponent.class,
                String.class,
                ExpressionType.SIMPLE,
                "event-(button|dropdown)"
        );
    }

    @Override
    protected String @NotNull [] get(@NotNull Event e) {

        final String id;
        if (isButton) {
            ButtonClick.EvtButtonClick event = (ButtonClick.EvtButtonClick) e;
            id = event.getJDAEvent().getButton().getId();
        } else {
            SelectionMenu.EvtSelectionMenu event = (SelectionMenu.EvtSelectionMenu) e;
            id = event.getJDAEvent().getComponent().getId();
        }

        return new String[] {id};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "event-" + (isButton ? "button" : "dropdown");
    }

    private NodeInformation node;
    private boolean isButton = false;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        node = new NodeInformation();

        if (!SkriptAdapter.getInstance().isCurrentEvents(
                ButtonClick.EvtButtonClick.class,
                SelectionMenu.EvtSelectionMenu.class
        )) {
            Skript.error("The event-component expression can only be used in a button or dropdown click event!");
            return false;
        }

        isButton = parseResult.expr.equalsIgnoreCase("event-button");

        if (isButton && !SkriptAdapter.getInstance().isCurrentEvents(
                ButtonClick.EvtButtonClick.class
        )) {
            Skript.error("The event-button expression can only be used in a button click event!");
            return false;
        }

        if (!isButton && !SkriptAdapter.getInstance().isCurrentEvents(
                SelectionMenu.EvtSelectionMenu.class
        )) {
            Skript.error("The event-dropdown expression can only be used in a dropdown click event!");
            return false;
        }

        return true;
    }
}
