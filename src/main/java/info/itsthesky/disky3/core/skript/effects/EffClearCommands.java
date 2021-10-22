package info.itsthesky.disky3.core.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Clear Commands")
@Description("Clear every registered global commands of a specific bot.")
@Examples("clear commands of event-bot")
public class EffClearCommands extends WaiterEffect {

    static {
        Skript.registerEffect(
                EffClearCommands.class,
                "clear [slash] command[s] of [the] [bot] %bot%"
        );
    }

    private Expression<Bot> exprBot;

    @Override
    public boolean initEffect(Expression[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprBot = (Expression<Bot>) expressions[0];
        return true;
    }

    @Override
    public void runEffect(Event e) {
        final Bot bot = exprBot.getSingle(e);
        if (bot == null) return;
        DiSky.debug("Deleting every commands of " + bot.getName() +"!");
        bot.getCore().updateCommands().queue();
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "clear slash commands of " + exprBot.toString(e, debug);
    }
}
