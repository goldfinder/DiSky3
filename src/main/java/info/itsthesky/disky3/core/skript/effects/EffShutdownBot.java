package info.itsthesky.disky3.core.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EffShutdownBot extends Effect {

    static {
        Skript.registerEffect(
                EffShutdownBot.class,
                "(close instance|shut[( |-)]down) [the] [bot] %bot%"
        );
    }

    private Expression<Bot> exprBot;

    @Override
    protected void execute(@NotNull Event e) {
        if (exprBot.getSingle(e) == null)
            return;
        exprBot.getSingle(e).shutdown();
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "shutdown bot " + exprBot.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprBot = (Expression<Bot>) exprs[0];
        return true;
    }

}
