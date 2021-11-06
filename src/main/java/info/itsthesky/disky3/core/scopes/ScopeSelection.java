package info.itsthesky.disky3.core.scopes;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.section.EffectSection;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ScopeSelection extends EffectSection {

    public static SelectionMenu.Builder lastBuilder;

    static {
        Skript.registerCondition(ScopeSelection.class, "make [new] [discord] [(1|disabled)] (select[s]|dropdown|selection menu) with [the] id %string%");
    }

    private Expression<String> exprInput;
    private boolean disabled = false;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (checkIfCondition()) return false;
        if (!hasSection()) return false;
        loadSection(true);
        disabled = parseResult.mark == 1;
        exprInput = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String input = exprInput.getSingle(e);
        if (input == null) return;
        lastBuilder = SelectionMenu.create(input).setDisabled(disabled);
        runSection(e);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "make new select with id " + exprInput.toString(e, debug);
    }

}
