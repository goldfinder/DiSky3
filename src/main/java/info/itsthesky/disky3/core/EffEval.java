package info.itsthesky.disky3.core;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.command.Commands;
import ch.njol.skript.config.Config;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.skript.parser.Evaluate;
import info.itsthesky.disky3.core.commands.Argument;
import info.itsthesky.disky3.core.commands.CommandFactory;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EffEval extends Effect {

    static {
//        Skript.registerEffect(
//                EffEval.class,
//                "eval[uate] %string% [and store (it|the error[s]) in %-objects%]"
//        );
    }

    private Variable<?> var;
    private List<Argument<?>> discordArgs;
    private List<ch.njol.skript.command.Argument<?>> cmdArgs;
    private Config script;
    private Expression<String> input;

    @Override
    protected void execute(@NotNull Event e) {
        try {
            Evaluate
                    .getInstance()
                    .evaluate(input.getSingle(e), e, var, true, script, cmdArgs, discordArgs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "ea";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        input = (Expression<String>) exprs[0];
        var = (Variable<?>) exprs[1];
        script = SkriptLogger.getNode().getConfig();
        cmdArgs = Commands.currentArguments;
        discordArgs = CommandFactory.currentArguments;
        return true;
    }
}
