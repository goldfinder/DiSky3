package info.itsthesky.disky3.core.components;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

@Name("Clear Message Buttons")
@Description("Clear every buttons of a specific message.")
public class EffClearButtons extends WaiterEffect {

    static {
        Skript.registerEffect(EffClearButtons.class,
                "(clear|remove|delete) [all] buttons from [the] [message] %message%");
    }

    private Expression<UpdatingMessage> exprMessage;

    @SuppressWarnings("unchecked")
    @Override
    public boolean initEffect(Expression @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        this.exprMessage = (Expression<UpdatingMessage>) exprs[0];
        return true;
    }

    @Override
    public void runEffect(@NotNull Event e) {
        UpdatingMessage message = exprMessage.getSingle(e);
        if (message == null) return;
        message.getMessage().editMessage(message.getMessage())
                .setActionRows(Collections.emptyList())
                .queue(msg -> {
                    UpdatingMessage.put(message.getID(), message.getMessage());
                    if (exprMessage instanceof Variable)
                        (exprMessage).change(e, new UpdatingMessage[] {message}, Changer.ChangeMode.SET);
                    restart();
                });
    }

    @Override
    public @NotNull String toString(Event e, boolean debug) {
        return "clear all buttons from message " + exprMessage.toString(e, debug);
    }

}
