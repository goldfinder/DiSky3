package info.itsthesky.disky3.api.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CloneEffect<E> extends WaiterEffect<E> {

    protected static void register(
            Class<? extends Effect> clazz,
            String infoName
    ) {
        Skript.registerEffect(
                clazz,
                "(copy|clone) [the] [discord] ["+infoName+"] %"+infoName+"% [(with|using) [bot] %-bot%] and store (it|the "+infoName+") in %object%"
        );
    }

    private Expression<E> exprOriginal;
    private Expression<Bot> exprBot;

    @Override
    public boolean initEffect(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {

        exprOriginal = (Expression<E>) exprs[0];
        exprBot = (Expression<Bot>) exprs[1];
        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);
        if (exprBot == null) {
            Skript.error("Unable to get the bot in a clone entity effect.");
            return false;
        }

        Variable<E> variable = (Variable<E>) Utils.parseVar(exprs[2], true);
        if (variable == null)
            return false;
        setChangedVariable(variable);
        return true;
    }

    public abstract AuditableRestAction<E> clone(E entity, Bot bot);

    @Override
    public void runEffect(Event e) {
        final E entity = exprOriginal.getSingle(e);
        final Bot bot = exprBot.getSingle(e);
        if (entity == null || bot == null)
            return;
        AuditableRestAction<E> action = clone(entity, bot);
        action.queue(this::restart);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "clone " + exprOriginal.toString(e, debug) + " using bot " + exprBot.toString(e, debug);
    }
}
