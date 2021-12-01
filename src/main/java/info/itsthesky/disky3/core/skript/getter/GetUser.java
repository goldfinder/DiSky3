package info.itsthesky.disky3.core.skript.getter;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.Constants;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("User with ID / Name")
@Description({"Get a user object from its ID or through its Name & tag formatted as: XXXXXX#000",
        "Remember users and members are not the same object, have different properties!",
        "More information can be found here: " + Constants.GITHUB_WIKI_BASICS_USER_MEMBER_DIFFERENCE,
        "",
        "This expression return a cached user, so can sometimes return null if the user wasn't cached!",
        "More information about caching: " + Constants.GITHUB_WIKI_BASICS_RETRIEVING_GETTING})
@Examples({"set {_m} to user with id \"000\"",
        "set {_m} to user from id \"000\" with event-bot",
        "set {_m} to user with name \"ItsTheSky##1234\""})
public class GetUser extends SimpleExpression<User> {

    static {
        Skript.registerExpression(
                GetUser.class,
                User.class,
                ExpressionType.SIMPLE,
                "user (with|from) id %string% [(with|using) [bot] %-bot%]",
                "user (with|from) [the] name %string% [(with|using) [bot] %-bot%]"
        );
    }

    private boolean fromName;
    private Expression<String> exprId;
    private Expression<Bot> exprBot;
    private NodeInformation node;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        node = new NodeInformation();
        exprId = (Expression<String>) exprs[0];
        exprBot = (Expression<Bot>) exprs[1];
        fromName = matchedPattern == 1;

        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);

        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "user with id " + exprId.toString(e, debug);
    }

    @Override
    protected User @NotNull [] get(@NotNull Event e) {
        final String id = exprId.getSingle(e);
        final Bot bot = Utils.verifyVar(e, exprBot);
        if (id == null || bot == null) return new User[0];

        if (fromName) {
            final String[] values = id.split("#");
            if (values.length <= 1) {
                DiSky.exception(new DiSkyException("The specified name ('"+id+"') doesn't match the pattern 'XXXXXX#0000'!"), node);
                return new User[0];
            }
            final String name = values[0];
            final String tag = values[1];
            if (tag.length() != 4) {
                DiSky.exception(new DiSkyException("The specified name ('"+id+"') have more or less than 4 chars after the '#' !"), node);
                return new User[0];
            }
            return new User[] {bot.getCore().getUserByTag(name, tag)};
        } else {
            return new User[] {bot.getCore().getUserById(id)};
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends User> getReturnType() {
        return User.class;
    }
}
