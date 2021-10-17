package info.itsthesky.disky3.api.skript.action;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unchecked")
public abstract class CreateAction<E, T extends AuditableRestAction<E>> extends WaiterEffect<E> {

    protected static void register(
            Class<? extends Effect> clazz,
            String actionType
    ) {
        Skript.registerEffect(
                clazz,
                "create [the] [(action|manager)] %"+actionType+"% and store (it|the "+ (actionType.replaceAll("action", "")) +") in %object%"
        );
    }

    private Expression<T> exprAction;

    @Override
    public boolean initEffect(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprAction = (Expression<T>) expressions[0];
        setChangedVariable((Variable<E>) expressions[1]);
        return true;
    }

    @Override
    public void runEffect(Event e) {
        final T action = Utils.verifyVar(e, exprAction, null);
        if (action == null) {
            restart();
            return;
        }
        action.queue(en -> restart((E) en));
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return null;
    }
}
