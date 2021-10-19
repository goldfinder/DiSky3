package info.itsthesky.disky3.core.sections;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import info.itsthesky.disky3.api.skript.section.EventSection;
import info.itsthesky.disky3.api.skript.section.EventWaiter;
import info.itsthesky.disky3.core.events.component.ButtonClick;
import info.itsthesky.disky3.core.events.component.SelectionMenu;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Dropdown Section")
@Description("Make a specific dropdown execute a given Skript code. This code will only be executed once the messages is sent!")
public class DropdownSection extends EventSection<SelectionMenuEvent> {

    static {
        register(
                DropdownSection.class,
                "make [the] [dropdown] %selectbuilder% [run]"
        );
    }

    private Expression<net.dv8tion.jda.api.interactions.components.selections.SelectionMenu.Builder> exprDropdown;

    @Override
    protected EventWaiter<SelectionMenuEvent> runEffect(Event e) {
        final net.dv8tion.jda.api.interactions.components.selections.SelectionMenu.Builder dropdown = exprDropdown.getSingle(e);
        if (dropdown == null)
            return null;

        final SelectionMenu.EvtSelectionMenu event = new SelectionMenu.EvtSelectionMenu(new SelectionMenu());
        return new EventWaiter<>(
                SelectionMenuEvent.class,
                ev -> {
                    event.setJDAEvent(ev);
                    runSection(event);
                    if (event.isCancelled())
                        ev.deferEdit().queue();
                },
                ev -> ev.getComponent().getId().equalsIgnoreCase(dropdown.getId())
        );
    }

    @Override
    protected Class<? extends Event> getSectionEvent() {
        return SelectionMenu.EvtSelectionMenu.class;
    }

    @Override
    protected void loadExpressions(Expression<?>[] exprs) {
        exprDropdown = (Expression<net.dv8tion.jda.api.interactions.components.selections.SelectionMenu.Builder>) exprs[0];
    }

    @Override
    protected String getSectionName() {
        return "dropdown section";
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "make dropdown " + exprDropdown.toString(e, debug) + " run code";
    }
}
