package info.itsthesky.disky3.api.skript.action;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractNewAction<T, E> extends SimpleExpression<T> {

    private Expression<E> exprGuild;

    protected abstract T create(@NotNull E guild);

    @Override
    protected T @NotNull [] get(@NotNull Event e) {
        final E guild = Utils.verifyVar(e, exprGuild);
        if (guild == null)
            return (T[]) new Object[0];
        return (T[]) new Object[] {create(guild)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    public abstract String getNewType();

    public abstract String entityToString(Expression<E> entity, Event e, boolean debug);

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "a new "+ getNewType() +" action " + entityToString(exprGuild, e,  debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprGuild = (Expression<E>) exprs[0];
        return true;
    }

}
