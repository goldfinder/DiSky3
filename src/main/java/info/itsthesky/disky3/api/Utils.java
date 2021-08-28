package info.itsthesky.disky3.api;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Variable;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * All these code were made & are owned by ItsTheSky!
 * @author ItsTheSky
 */
public final class Utils {

    public static String colored(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static <T> T verifyVar(@NotNull Event e, @Nullable Expression<T> expression) {
        return verifyVar(e, expression, null);
    }

    public static <T> T verifyVar(@NotNull Event e, @Nullable Expression<T> expression, T defaultValue) {
        return expression == null ? defaultValue : (expression.getSingle(e) == null ? defaultValue : expression.getSingle(e));
    }

    public static @Nullable Variable<?> parseVar(Expression<?> expression, boolean shouldBeList) {
        if (expression instanceof Variable<?>) {
            if (shouldBeList && !((Variable<?>) expression).isList()) {
                Skript.error("The specified variable must be a list!");
                return null;
            }
            return (Variable<?>) expression;
        }
        Skript.error("You must specific a valid variable, but got " + expression.toString());
        return null;
    }

    public static @Nullable Variable<?> parseVar(Expression<?> expression) {
        return parseVar(expression, false);
    }

}
