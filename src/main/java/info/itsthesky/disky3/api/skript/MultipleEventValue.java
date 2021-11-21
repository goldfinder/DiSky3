package info.itsthesky.disky3.api.skript;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Here to handle event-value that should return more that one element.
 * @author Sky
 * @param <T> The entity type, like 'Role'
 * @param <E> The event type to get the value from, like 'EvtRoleAdd'
 */
public abstract class MultipleEventValue<T, E extends Event> extends SimpleExpression<T> {

    public static <R> void register(
            Class<? extends MultipleEventValue<R, ?>> clazz,
            Class<R> entityClazz,
            String name
    ) {
        Skript.registerExpression(
                clazz,
                entityClazz,
                ExpressionType.PATTERN_MATCHES_EVERYTHING,
                "[the] past event[-]"+ name,
                "[the] event[-]"+ name,
                "[the] future event[-]"+ name
        );
    }

    private ValueAge valueAge;

    protected abstract List<T> convert(E event, ValueAge age);

    /**
     * The entity name, should contains plural expression.
     * <br> e.g. 'role' for 'event-roles', etc...
     */
    protected abstract String getEntityName();

    protected abstract Class<E>[] getEventClasses();

    protected List<ValueAge> getValueAge() {
        return Collections.singletonList(ValueAge.CURRENT);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T @NotNull [] get(@NotNull Event e) {
        return (T[]) convert((E) e, valueAge).toArray(new Object[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the event-" + getEntityName();
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (!SkriptAdapter.getInstance().isCurrentEvents(getEventClasses())) {
            Skript.error("The event-" + getEntityName() + " cannot be used in a " + ScriptLoader.getCurrentEventName());
            return false;
        }
        valueAge = ValueAge.fromMark(matchedPattern);
        return getValueAge().contains(valueAge);
    }

    public enum ValueAge {
        PAST(0),
        CURRENT(1),
        FUTURE(2),
        ;

        private final int mark;

        ValueAge(int mark) {
            this.mark = mark;
        }

        public static @NotNull ValueAge fromMark(int mark) {
            return Arrays
                    .stream(values())
                    .filter(age -> age.getMark() == mark)
                    .findAny()
                    .orElse(CURRENT);
        }

        public int getMark() {
            return mark;
        }
    }
}
