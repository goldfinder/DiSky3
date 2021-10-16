package info.itsthesky.disky3.core.skript.advanced;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.effects.EffReturn;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.section.EffectSection;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Case extends EffectSection {

    static {
        register(
                Case.class,
                "case %string%",
                "default"
        );
    }

    public boolean isDefault;
    public boolean hasReturn;
    private Expression<String> caseExpression;

    public boolean shouldExecute(String input, Event e) {
        if (isDefault)
            return true;
        final String s = Utils.verifyVar(e, caseExpression, "");
        return !s.isEmpty() && input.equalsIgnoreCase(s);
    }

    @Override
    protected void execute(Event e) {

    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return (isDefault ? "default" : "case " + caseExpression.toString(e, debug));
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (!hasSection())
            Skript.warning("This case section doesn't have any code inside, so you can remove it.");
        if (checkIfCondition())
            return false;

        if (!isCurrentSection(Switch.class)) {
            Skript.error("The case section cannot be used outside a switch section!");
            return false;
        }
        final Switch switchSection = getCurrentSection(Switch.class);
        switchSection.registerCase(this);
        hasReturn = Utils.convert(this.getSectionNode().iterator()).stream().anyMatch(n -> n.getKey().contains("return"));
        System.out.println("Has return effect: " + hasReturn);

        isDefault = matchedPattern == 1;
        loadSection((isDefault ? "default" : "case"), true, ScriptLoader.getCurrentEvents());
        if (!isDefault)
            caseExpression = (Expression<String>) exprs[0];
        return true;
    }
}
