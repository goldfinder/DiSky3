package info.itsthesky.disky3.api.section;

import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import ch.njol.skript.ScriptLoader;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.core.skript.ExprException;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Effect that could get exception (like not enough permission).
 * <br> The desired exception code will be executed when any exception is handled.
 * @author ItsTheSky
 */
public abstract class RestExceptionSection<T> extends EffectSection {

    /**
     * The code which should return the rest action to handle
     * @param e The Skript event
     */
    public abstract RestAction<T> runRestAction(Event e);

    /**
     * An optional code to run if everything has been running correctly.
     */
    public Consumer<T> successConsumer() {
        return null;
    }

    private Object lastMap;

    @Override
    protected void execute(Event e) {
        lastMap = Variables.removeLocals(e);
        Variables.setLocalVariables(e, lastMap);
        try {
            final RestAction<T> action = runRestAction(e);
            action.queue(successConsumer(), ex -> handle(e, ex));
            handle(e, null);
        } catch (Throwable ex) {
            handle(e, ex);
        }
    }

    protected void handle(Event e, Throwable exception) {
        Variables.setLocalVariables(e, lastMap);
        ExprException.lastException = exception;
        runSection(e);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (checkIfCondition())
            return false;
        loadSection("error handler", true, SkriptAdapter.getInstance().getCurrentEvents());
        return true;
    }
}
