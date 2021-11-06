package info.itsthesky.disky3.core.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.DiSkyType;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.core.Types;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Name("All Value")
@Description({"Get all values that an enumeration could have, to store or loop them.",
"This only support DiSky's enumeration, such as permissions and regions, and won't work on another addon's enumeration."})
public class ExprAll extends SimpleExpression<Object> {

    private static final HashMap<String, Class<? extends Enum>> enums;

    static {
        final List<Class<? extends Enum>> temps = Types
                .DISKY_TYPES
                .stream()
                .filter(type -> type.isEnum())
                .map(DiSkyType::getEnumClass)
                .collect(Collectors.toList());
        enums = new HashMap<>();
        for (Class<? extends Enum> e : temps)
            enums.put(e.getSimpleName().toLowerCase(Locale.ROOT), e);
        Skript.registerExpression(
                ExprAll.class,
                Object.class,
                ExpressionType.SIMPLE,
                "all [values of] ("+ Utils.join(enums.keySet().toArray(new String[0]), "|")+")"
        );
    }

    private Class<? extends Enum> enumClazz;

    @Override
    protected Object @NotNull [] get(@NotNull Event e) {
        return enumClazz.getEnumConstants();
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<?> getReturnType() {
        return enumClazz;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "all values of " + enumClazz.getSimpleName();
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        final String input = parseResult.expr
                .replaceAll("all ", "")
                .replaceAll("values of ", "");
        final Class<? extends Enum> e = enums.getOrDefault(input, null);
        if (e == null) {
            Skript.error("Cannot found enumeration with name: " + input);
            return false;
        }
        this.enumClazz = e;
        return true;
    }
}
