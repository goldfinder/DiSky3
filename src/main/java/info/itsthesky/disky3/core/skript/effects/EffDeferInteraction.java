package info.itsthesky.disky3.core.skript.effects;

import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.skript.NodeInformation;
import info.itsthesky.disky3.api.skript.events.specific.InteractionEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Name("Defer Interaction")
@Description({"Only usable in interaction event, currently button click & dropdown update event!",
"\nThis will force the interaction to be acknowledge, sending a success message to Discord.",
"\nKeep in mind replying in an interaction event will automatically deferring the interaction, and therefore you won't be abel to defr it another time.",
"\nAn interaction can only be deferring once!"})
@Examples("defer the interaction")
public class EffDeferInteraction extends Effect {

    static {
        Skript.registerEffect(
                EffDeferInteraction.class,
                "(acknowledge|defer) [the] interaction"
        );
    }


    @Override
    protected void execute(@NotNull Event e) {
        GenericInteractionCreateEvent event = ((InteractionEvent) e).getInteractionEvent();

        if (event instanceof ButtonClickEvent) {

            ButtonClickEvent clickEvent = (ButtonClickEvent) event;
            clickEvent.deferEdit().queue(null, ex -> DiSky.exception(ex, node));

        } else {

            SelectionMenuEvent clickEvent = (SelectionMenuEvent) event;
            clickEvent.deferEdit().queue(null, ex -> DiSky.exception(ex, node));

        }

    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "defer the interaction";
    }

    private NodeInformation node;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {

        if (!(Arrays.asList(SkriptAdapter.getInstance().getCurrentEvents()[0].getInterfaces()).contains(InteractionEvent.class))) {
            Skript.error("The defer interaction effect can only be used in interaction events!");
            return false;
        }

        node = new NodeInformation();

        return true;
    }
}
