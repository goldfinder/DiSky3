package info.itsthesky.disky3.core.skript.getter;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RetrieveUser extends WaiterEffect<User> {

    static {
        Skript.registerEffect(
                RetrieveUser.class,
                "retrieve [the] user (from|with) id %string% [with [the] [bot] %-bot%] and store (it|the user) in %object%"
        );
    }

    private Expression<String> exprID;
    private Expression<Bot> exprBot;

    @Override
    public boolean initEffect(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprID = (Expression<String>) exprs[0];
        exprBot = (Expression<Bot>) exprs[1];
        setChangedVariable((Variable<User>) exprs[2]);

        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);

        if (exprBot == null) {
            DiSky.exception(new DiSkyException("The bot cannot be found in a retrieve effect!"), getNode());
            return false;
        }

        return true;
    }

    @Override
    public void runEffect(Event e) {
        Bot bot = exprBot.getSingle(e);
        String id = exprID.getSingle(e);
        if (id == null || bot == null) return;

        bot.getCore().retrieveUserById(id).queue(this::restart, ex -> {
            DiSky.exception(ex, getNode());
            restart();
        });
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "retrieve user with id " + exprID.toString(e, debug);
    }
}
