package info.itsthesky.disky3.core.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.ReflectionUtils;
import info.itsthesky.disky3.api.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.internal.entities.TextChannelImpl;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprName extends SimpleExpression<String> {

    static {
        Skript.registerExpression(
                ExprName.class,
                String.class,
                ExpressionType.SIMPLE,
               "[the] discord name of [entity] %user/member%"
        );
    }

    private Expression<Object> exprEntity;

    @Override
    protected String @NotNull [] get(@NotNull Event e) {
        Object name = Utils.verifyVar(e, exprEntity, null);
        final String n = Utils.getName(name);
        return n == null ? new String[0] : new String[] {n};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "discord name of " + exprEntity.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprEntity = (Expression<Object>) exprs[0];
        return true;
    }
}
