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
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Discord Name")
@Description({"Return the basic discord name of a specific entity.",
        "It will return what the client will see in the Discord client (channel, guild, etc...) except for the member, who will be converted to an user before;",
        "This mean 'discord id of %member%' will return the member's name on discord and not in a guild (even if a nickname could be present)."})
@Examples({"discord name of event-user",
        "discord name of event-member",
        "channel name of event-channel",
        "guild name of event-guild",
        "# etc..."})
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
        final String n = Utils.getName(name, true);
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
