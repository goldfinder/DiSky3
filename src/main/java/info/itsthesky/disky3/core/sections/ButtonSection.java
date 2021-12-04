package info.itsthesky.disky3.core.sections;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.TriggerSection;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.emojis.Emote;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.section.EventSection;
import info.itsthesky.disky3.api.skript.section.EventWaiter;
import info.itsthesky.disky3.core.events.component.ButtonClick;
import info.itsthesky.disky3.core.events.message.ReactionAdd;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

@Name("Button Section")
@Description("Make a specific button execute a given Skript code. This code will only be executed once the messages is sent!")
public class ButtonSection extends EventSection<ButtonClickEvent> {

    static {
        register(
                ButtonSection.class,
                "make [the] [button] %button% [run]"
        );
    }

    private Expression<Button> exprButton;

    @Override
    protected EventWaiter<ButtonClickEvent> runEffect(Event e) {
        final Button button = exprButton.getSingle(e);
        if (button == null)
            return null;

        final ButtonClick.EvtButtonClick event = new ButtonClick.EvtButtonClick(new ButtonClick());
        return new EventWaiter<>(
                ButtonClickEvent.class,
                ev -> {
                    event.setJDAEvent(ev);
                    runSection(event);
                    if (event.isCancelled())
                        ev.deferEdit().queue();
                },
                ev -> ev.getButton().getId().equalsIgnoreCase(button.getId()),
                "button-section-" + button.getId()
        );
    }

    @Override
    protected Class<? extends Event> getSectionEvent() {
        return ButtonClick.EvtButtonClick.class;
    }

    @Override
    protected void loadExpressions(Expression<?>[] exprs) {
        exprButton = (Expression<Button>) exprs[0];
    }

    @Override
    protected String getSectionName() {
        return "button section";
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "make button " + exprButton.toString(e, debug) + " run code";
    }
}
