package info.itsthesky.disky3.core.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.internal.utils.Checks;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ItsTheSky
 */
public class ParsedString extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(
                ParsedString.class,
                Object.class,
                ExpressionType.SIMPLE,
                "%strings% parsed as <([\\w- _]+)>"
        );
    }

    private Expression<String> input;
    private ClassInfo<?> classInfo;

    @Override
    protected Object @NotNull [] get(@NotNull Event e) {
        final List<Object> entities = new ArrayList<>();

        if (classInfo == null)
            return new Object[0];

        for (String s : input.getArray(e))
        {
            Object entity;
            entity = classInfo.getParser().parse(s, ParseContext.DEFAULT);
            if (entity != null) {
                entities.add(entity);
                continue;
            }
            entity = classInfo.getParser().parse(s, ParseContext.SCRIPT);
            if (entity != null) {
                entities.add(entity);
                continue;
            }
            entity = classInfo.getParser().parse(s, ParseContext.COMMAND);
            if (entity != null) {
                entities.add(entity);
            }
        }

        return entities.toArray(new Object[0]);
    }

    @Override
    public boolean isSingle() {
        return input.isSingle();
    }

    @Override
    public @NotNull Class<?> getReturnType() {
        return classInfo.getC();
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return input.toString(e, debug) + " parsed as " + classInfo.getCodeName();
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        input = (Expression<String>) exprs[0];

        String classInfoInput = parseResult.regexes.get(0).group(1);
        classInfo = Classes.getClassInfoFromUserInput(classInfoInput);

        if (classInfo == null) {
            Skript.error("Unknown class info type: " + classInfoInput);
            return false;
        }

        if (classInfo.getParser() == null || classInfo.getParser().canParse(ParseContext.SCRIPT)) {
            Skript.error("Cannot parse class info type: " + classInfoInput);
            return false;
        }

        return true;
    }
}
