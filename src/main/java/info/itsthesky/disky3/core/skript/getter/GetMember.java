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
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Member with ID")
@Description({"Get a member object from its ID and its guild.",
        "Remember users and members are not the same object, have different properties!",
        "More information can be found here: " + Constants.GITHUB_WIKI_BASICS_USER_MEMBER_DIFFERENCE,
        "You MUST specify the guild in order to get the more accurate member possible!",
        "",
        "This expression return a cached member, so can sometimes return null if the memebr wasn't cached!",
        "More information about caching: " + Constants.GITHUB_WIKI_BASICS_RETRIEVING_GETTING})
@Examples({"set {_m} to member with id \"000\" in event-guild",
        "set {_m} to member from id \"000\" in event-guild with event-bot"})
public class GetMember extends SimpleExpression<Member> {

    static {
        Skript.registerExpression(
                GetMember.class,
                Member.class,
                ExpressionType.SIMPLE,
                "member (with|from) id %string% (of|in|from) [the] [guild] %guild% [(with|using) [bot] %-bot%]"
        );
    }

    private Expression<String> exprId;
    private Expression<Guild> exprGuild;
    private Expression<Bot> exprBot;
    private NodeInformation node;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        node = new NodeInformation();
        exprId = (Expression<String>) exprs[0];
        exprGuild = (Expression<Guild>) exprs[1];
        exprBot = (Expression<Bot>) exprs[2];

        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);

        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "category with id " + exprId.toString(e, debug);
    }

    @Override
    protected Member @NotNull [] get(@NotNull Event e) {
        final String id = exprId.getSingle(e);
        final Bot bot = Utils.verifyVar(e, exprBot);
        Guild guild = exprGuild.getSingle(e);
        if (id == null || guild == null)
            return new Member[0];

        if (bot != null)
            guild = bot.getCore().getGuildById(guild.getId());

        return new Member[] {guild.getMemberById(id)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Member> getReturnType() {
        return Member.class;
    }
}
