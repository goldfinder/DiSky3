package info.itsthesky.disky3.core.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import net.dv8tion.jda.api.entities.IMentionable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class ExprMention extends SimpleExpression<String> {

    static {
        Skript.registerExpression(
                ExprMention.class,
                String.class,
                ExpressionType.SIMPLE,
               "[the] [discord] mention [tag] of [entity] %objects%"
        );
    }

    private Expression<IMentionable> exprEntity;

    @Override
    protected String @NotNull [] get(@NotNull Event e) {
        final Object[] entities = exprEntity.isSingle() ? new Object[] {exprEntity.getSingle(e)} : exprEntity.getArray(e);
        try {
            return Arrays
                    .stream(entities)
                    .map(entity -> (IMentionable) entity)
                    .map(IMentionable::getAsMention)
                    .toArray(String[]::new);
        } catch (ClassCastException ex) {
            return new String[0];
        }
    }

    @Override
    public boolean isSingle() {
        return exprEntity.isSingle();
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "mention tag of " + exprEntity.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprEntity = (Expression<IMentionable>) exprs[0];
        return true;
    }
}
