package info.itsthesky.disky3.core;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.skript.parser.ParserUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Eval")
@Description({"Evaluate through Skript a specific amount of code line.",
"This effect will parse, then possibly execute the given code.",
"The variable will store the potential error that occurred while parsing.",
"You can only execute STATEMENT, so effects and conditions;",
"Events, commands, functions, etc... cannot be evaluated!"})
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
        List<String> errors = new ArrayList<>();
        try {
            errors = ParserUtils.parse(input.getSingle(e), e, execute, data);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
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
