package info.itsthesky.disky3.core.skript.errorhandler;


import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.*;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.section.EffectSection;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SectionCatch extends EffectSection {

    // static {
    //     Skript.registerCondition(
    //             SectionCatch.class,
    //             "catch [the] [exception] as %object%"
    //     );
    // }

    private Variable<?> exceptionVariable;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (!hasSection())
            return false;
        if (checkIfCondition())
            return false;

        boolean hasTry = getRelatedTry(getNode().getParent());
        if (!hasTry) {
            Skript.error("Cannot use a 'catch' section without a 'try' section before.");
            return false;
        }

        loadSection("catch", false, SkriptAdapter.getInstance().getCurrentEvents());

        if (Utils.parseVar(exprs[0], false, true) == null)
            return false;
        exceptionVariable = Utils.parseVar(exprs[0], false, true);
        return true;
    }

    protected void catchException(Event e, Throwable ex) {
        System.out.println(ex.getMessage());
        Object previousMap = Variables.removeLocals(e);
        exceptionVariable.change(e, new String[] {ex.getMessage()}, Changer.ChangeMode.SET);
        Variables.setLocalVariables(e, previousMap);
        runSection(e);
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event e) {
        return null;
    }

    private boolean getRelatedTry(SectionNode parent) {
        for (Node node : parent) {
            if (node.getKey().startsWith("try")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "try";
    }

    @Override
    protected void execute(Event e) { }
}
