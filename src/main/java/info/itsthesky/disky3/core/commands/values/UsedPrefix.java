package info.itsthesky.disky3.core.commands.values;

import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.core.commands.CommandEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Used Prefix")
@Description("Return the used prefix in a discord command trigger section.")
public class UsedPrefix extends SimpleExpression<String> {

    static {
        Skript.registerExpression(
                UsedPrefix.class,
                String.class,
                ExpressionType.SIMPLE,
                "[the] use[d]( |-)prefix[es]"
        );
    }

    @Override
    protected String @NotNull [] get(@NotNull Event e) {
        return new String[] {((CommandEvent) e).getPrefix()};
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
        return "the used alias";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (!ScriptLoader.isCurrentEvent(CommandEvent.class)) {
            Skript.error("The used prefix can only used in a discord command trigger section.");
            return false;
        }
        return true;
    }
}
