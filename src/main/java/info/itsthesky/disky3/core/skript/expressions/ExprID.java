package info.itsthesky.disky3.core.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import net.dv8tion.jda.api.entities.ISnowflake;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Discord ID")
@Description({
        "Get the discord ID related to a discord object, such as user, emoji, channel and more.",
        "The returned ID will always be a long value (like \"0326478972\"), except with Emoji where the ID returned is the emoji's name (like \"joy\")."
})
@Examples({"" +
        "discord id of event-user",
        "discord id of reaction \"joy\" # Will return 'joy' xD"
})
public class ExprID extends SimpleExpression<String> {

    static {
        Skript.registerExpression(
                ExprID.class,
                String.class,
                ExpressionType.SIMPLE,
               "[the] [discord] id of [entity] %object%"
        );
    }

    private Expression<ISnowflake> exprEntity;

    @Override
    protected String @NotNull [] get(@NotNull Event e) {
        try {
            ISnowflake mention = Utils.verifyVar(e, exprEntity, null);
            if (mention == null)
                return new String[0];
            return new String[]{mention.getId()};
        } catch (ClassCastException ex) {
            return new String[0];
        }
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
        return "discord id of " + exprEntity.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprEntity = (Expression<ISnowflake>) exprs[0];
        return true;
    }
}
