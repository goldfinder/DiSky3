package info.itsthesky.disky3.core.skript.advanced;

import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.section.EffectSection;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Switch extends EffectSection {

    private final List<Case> cases = new ArrayList<>();
    private boolean hasReturned = false;

    public void registerCase(Case c) {
        this.cases.add(c);
    }

    static {
        register(
                Switch.class,
                "switch [the] [str] %string%"
        );
    }

    @Override
    protected void execute(Event e) {
        runSection(e);
        final String value = exprValue.getSingle(e);
        if (value == null) {
            final Case defaultCase = cases.stream().filter(c -> c.isDefault).findAny().orElse(null);
            if (defaultCase != null)
                defaultCase.execute(e);
            return;
        }
        for (Case c : cases) {
            if (!c.shouldExecute(value, e))
                continue;
            System.out.println(hasReturned);
            if (hasReturned)
                return;
            if (c.hasReturn)
                hasReturned = true;
            c.runSection(e);
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "switch " + exprValue.toString(e, debug);
    }

    private Expression<String> exprValue;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (!hasSection())
            Skript.warning("This switch doesn't have any code inside, so you can remove it.");
        if (checkIfCondition())
            return false;
        loadSection("switch", true, SkriptAdapter.getInstance().getCurrentEvents());
        exprValue = (Expression<String>) exprs[0];
        return true;
    }
}
