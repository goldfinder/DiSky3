package info.itsthesky.disky3.core.commands.values;

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

@Name("Used Alias")
@Description("Return the used alias in a discord command trigger section.")
public class UsedAlias extends SimpleExpression<String> {

    static {
        Skript.registerExpression(
                UsedAlias.class,
                String.class,
                ExpressionType.SIMPLE,
                "[the] use[d]( |-)alias[es]"
        );
    }

    @Override
    protected String @NotNull [] get(@NotNull Event e) {
        return new String[] {((CommandEvent) e).getUsedAlias()};
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
            Skript.error("The used alias can only used in a discord command trigger section.");
            return false;
        }
        return true;
    }
}