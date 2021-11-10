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
public abstract class GuildAction<T> extends AbstractNewAction<T, Guild> {

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

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String entityToString(Expression<Guild> entity, Event e, boolean debug) {
        return "in guild " + entity.toString(e, debug);
    }
}
