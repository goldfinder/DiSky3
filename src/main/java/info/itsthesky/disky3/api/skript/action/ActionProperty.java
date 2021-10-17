package info.itsthesky.disky3.api.skript.action;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.changers.ChangeablePropertyExpression;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class ActionProperty<E, T extends AuditableRestAction<E>, O> extends ChangeablePropertyExpression<Object, O> {

    public void updateEntity(T newAction, Event event) {
        getExpr().change(event, newAction == null ? new Object[0] : new Object[] {newAction}, Changer.ChangeMode.SET);
    }

    @Override
    public void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0 || delta[0] == null)
            return;
        final O value = (O) delta[0];
        final Object entity = Utils.verifyVar(e, getExpr(), null);
        try {
            change((E) entity, value);
        } catch (ClassCastException ex) {
            updateEntity(change((T) entity, value), e);
        }
    }

    public abstract void change(E role, O value);

    public abstract T change(T action, O value);

    public abstract O get(E role);

    @Override
    protected O @NotNull [] get(@NotNull Event e, Object @NotNull [] source) {
        return (O[]) Arrays.stream(source).map(object -> object instanceof Role ? get((E) object) : null).toArray(Object[]::new);
    }

    NodeInformation information;

    public NodeInformation getNode() {
        return information;
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr(exprs[0]);
        information = new NodeInformation();
        return true;
    }
    
}
