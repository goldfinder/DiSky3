package info.itsthesky.disky3.core.skript.getter;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Retrieve Profile")
@Description({"Retrieve the profile of the specified user.",
        "Profile represent mainly the banner of the user, could return the accent color if non set."})
public class RetrieveProfile extends WaiterEffect<User.Profile> {

    static {
        Skript.registerEffect(
                RetrieveProfile.class,
                "retrieve [the] [user] profile (from|of|in) %user% [with [the] [bot] %-bot%] and store (it|the user) in %object%"
        );
    }

    private Expression<User> exprUser;
    private Expression<Bot> exprBot;

    @Override
    public boolean initEffect(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprUser = (Expression<User>) exprs[0];
        exprBot = (Expression<Bot>) exprs[1];
        setChangedVariable((Variable<User.Profile>) exprs[2]);

        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);

        if (exprBot == null) {
            DiSky.exception(new DiSkyException("The bot cannot be found in a retrieve profile effect!"), getNode());
            return false;
        }

        return true;
    }

    @Override
    public void runEffect(Event e) {
        Bot bot = exprBot.getSingle(e);
        User target = exprUser.getSingle(e);
        if (target == null || bot == null) return;

        bot
                .getCore()
                .retrieveUserById(target.getId())
                .queue(user -> {
                    user.retrieveProfile().queue(this::restart);
                });
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "retrieve profile of " + exprUser.toString(e, debug);
    }
}
