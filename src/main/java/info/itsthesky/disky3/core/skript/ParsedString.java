package info.itsthesky.disky3.core.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.Kleenean;
import ch.njol.util.StringUtils;
import info.itsthesky.disky3.core.Types;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ItsTheSky
 */
@Name("Parsed As")
@Description({"Parse a string to the desired class info of DiSky.",
"This expression will take the DiSky's parser used in discord command factory in order to have the more accurate parsed entity possible."})
public class ParsedString extends SimpleExpression<Object> {

    static {
        final List<String> infos = Types.DISKY_TYPES
                .stream()
                .filter(info -> info.getParser() != null && (
                        info.getParser().canParse(ParseContext.COMMAND) ||
                                info.getParser().canParse(ParseContext.COMMAND) ||
                                info.getParser().canParse(ParseContext.DEFAULT) ||
                                info.getParser().canParse(ParseContext.SCRIPT) ||
                                info.getParser().canParse(ParseContext.CONFIG) ||
                                info.getParser().canParse(ParseContext.EVENT)

                ))
                .map(ClassInfo::getCodeName)
                .filter(codeName -> !codeName.equalsIgnoreCase("member"))
                .collect(Collectors.toList());
        Skript.registerExpression(
                ParsedString.class,
                Object.class,
                ExpressionType.SIMPLE,
                "%strings% parsed as ("+ StringUtils.join(infos.toArray(new String[0]), "|") +")",
                "%strings% parsed as member in [the] [guild] %guild%"
        );
    }

    private Expression<String> input;
    private ClassInfo<?> classInfo;
    private Expression<Guild> exprGuild;
    private boolean isMember = false;

    @Override
    protected Object @NotNull [] get(@NotNull Event e) {
        final List<Object> entities = new ArrayList<>();

        if (classInfo == null)
            return new Object[0];

        for (String s : input.getArray(e))
        {
            if (isMember) {
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
            } else {
                final Member member = Types.parseMember(s, exprGuild.getSingle(e));
                entities.add(member);
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
        isMember = matchedPattern == 1;
        if (isMember)
            exprGuild = (Expression<Guild>) exprs[1];

        String classInfoInput = parseResult.expr.split("parsed as ")[1].split(" ")[0];
        classInfo = Classes.getClassInfoFromUserInput(classInfoInput);

        if (classInfo == null) {
            Skript.error("Unknown class info type: " + classInfoInput);
            return false;
        }

        if (classInfo.getParser() == null || !classInfo.getParser().canParse(ParseContext.SCRIPT)) {
            Skript.error("Cannot parse class info type: " + classInfoInput);
            return false;
        }

        return true;
    }
}
