package info.itsthesky.disky3.core;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.command.Commands;
import ch.njol.skript.config.Config;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.skript.parser.Evaluate;
import info.itsthesky.disky3.api.skript.parser.ParserUtils;
import info.itsthesky.disky3.core.commands.Argument;
import info.itsthesky.disky3.core.commands.CommandFactory;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EffEval extends Effect {

    static {
        Skript.registerEffect(
                EffEval.class,
                "eval[uate] [without executing] %string% [and store (it|the error[s]) in %-objects%]"
        );
    }

    private Variable<?> var;
    private ParserUtils.ParserData data;
    private Expression<String> input;
    private boolean execute;

    @Override
    protected void execute(@NotNull Event e) {
        final List<String> errors = ParserUtils.parse(input.getSingle(e), e, execute, data);
        var.change(e, errors.toArray(new String[0]), Changer.ChangeMode.SET);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "ea";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        input = (Expression<String>) exprs[0];
        var = (Variable<?>) exprs[1];
        data = new ParserUtils.ParserData();
        execute = !parseResult.expr.contains("without executing");
        return true;
    }
}
