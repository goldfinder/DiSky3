package info.itsthesky.disky3.api.skript.section;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.TriggerSection;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.section.EffectSection;
import net.dv8tion.jda.api.events.GenericEvent;
import org.bukkit.event.Event;

import java.util.Arrays;
import java.util.List;

public abstract class EventSection<T extends GenericEvent> extends EffectSection {

    private Object lastMap;
    private boolean isOneTime;

    public static void register(
            Class<? extends EffectSection> clazz,
            String... pattern
    ) {
        pattern = Arrays.stream(pattern).map(eff -> eff + " [one time]").toArray(String[]::new);
        Skript.registerCondition(clazz, pattern);
    }

    @Override
    protected void execute(Event e) {
        lastMap = Variables.removeLocals(e);
        Variables.setLocalVariables(e, lastMap);
        final EventWaiter<T> waiter = runEffect(e);
        if (waiter != null)
            waiter.setOneTime(isOneTime);
    }

    @Override
    public void runSection(Event e) {
        this.executeNext = false;
        Variables.setLocalVariables(e, lastMap);
        TriggerItem.walk(trigger, e);
    }

    protected abstract EventWaiter<T> runEffect(Event e);

    protected abstract Class<? extends Event> getSectionEvent();

    protected abstract void loadExpressions(Expression<?>[] exprs);

    protected abstract String getSectionName();

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (checkIfCondition())
            return false;
        loadExpressions(exprs);
        loadSection(getSectionName(), false, getSectionEvent());
        isOneTime = parseResult.expr.endsWith("one time");
        return true;
    }
}
