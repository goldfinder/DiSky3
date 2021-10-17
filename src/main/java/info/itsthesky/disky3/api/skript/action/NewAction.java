package info.itsthesky.disky3.api.skript.action;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unchecked")
public abstract class NewAction<T> extends SimpleExpression<T> {

    protected static void register(
            Class clazz,
            Class entityClazz,
            String actionName
    ) {
        Skript.registerExpression(
                clazz,
                entityClazz,
                ExpressionType.SIMPLE,
                "[a] new "+actionName+" (action|manager) in [the] [guild] %guild%"
        );
    }

    private Expression<Guild> exprGuild;

    protected abstract T create(@NotNull Guild guild);

    @Override
    protected T @NotNull [] get(@NotNull Event e) {
        final Guild guild = Utils.verifyVar(e, exprGuild);
        if (guild == null)
            return (T[]) new Object[0];
        return (T[]) new Object[] {create(guild)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    public abstract String getNewType();

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "a new "+ getNewType() +" action in guild " + exprGuild.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprGuild = (Expression<Guild>) exprs[0];
        return true;
    }

}
