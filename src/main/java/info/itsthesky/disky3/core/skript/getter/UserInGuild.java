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
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("User in Guild")
@Description({"Get the member object of an user in a specific guild.",
        "This will simply convert the user into a member by specifying a guild, but could return null if the user never joined the guild."})
@Examples({"event-user in event-guild",
        "user with id \"000\" in event-guild # This is the same as 'member with id \"000\" in event-guild"})
public class UserInGuild extends SimpleExpression<Member> {

    static {
        Skript.registerExpression(
                UserInGuild.class,
                Member.class,
                ExpressionType.COMBINED,
                "[the] [user] %user% in [the] [guild] %guild% [(with|using) [the] [bot] %-bot%]"
        );
    }

    private Expression<User> exprUser;
    private Expression<Guild> exprGuild;
    private Expression<Bot> exprBot;

    @Override
    protected Member @NotNull [] get(@NotNull Event e) {
        User user = exprUser.getSingle(e);
        Guild guild = exprGuild.getSingle(e);
        final @Nullable Bot bot = Utils.verifyVar(e, exprBot);
        if (user == null || guild == null)
            return new Member[0];

        if (bot != null) {
            user = bot.getCore().getUserById(user.getId());
            guild = bot.getCore().getGuildById(guild.getId());
        }

        return new Member[] {guild.getMember(user)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Member> getReturnType() {
        return Member.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return exprUser.toString(e, debug) + " in guild " + exprGuild.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprUser = (Expression<User>) exprs[0];
        exprGuild = (Expression<Guild>) exprs[1];
        exprBot = Utils.defaultToEventValue(exprs[2], Bot.class);
        return true;
    }
}
