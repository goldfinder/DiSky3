package info.itsthesky.disky3.api.section;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.core.skript.ExprException;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.event.Event;
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
     * The (nullable) consumer to run when the code is ran successfully
     */
    protected @Nullable Consumer<T> successConsumer() {
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
        } catch (Exception ex) {
            handle(e, ex);
        }
    }

    protected void handle(Event e, Throwable exception) {
        Variables.setLocalVariables(e, lastMap);
        ExprException.lastException = exception;
        runSection(e);
        Variables.removeLocals(e);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (checkIfCondition())
            return false;
        loadSection("error handler", true, ScriptLoader.getCurrentEvents());
        return true;
    }
}
