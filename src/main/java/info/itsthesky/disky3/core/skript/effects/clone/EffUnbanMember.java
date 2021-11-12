package info.itsthesky.disky3.core.skript.effects.clone;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.section.RestExceptionSection;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EffUnbanMember extends RestExceptionSection<Void> {

    static {
        register(
                EffUnbanMember.class,
                "unban [the] [user] %user% from [the] [guild] %guild% [(with|using) [bot] %-bot%]"
        );
    }

    private Expression<User> exprUser;
    private Expression<Guild> exprGuild;
    private Expression<Bot> exprBot;

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "unban " + exprUser.toString(e, debug) + " from guild " + exprGuild.toString(e, debug) + " using bot " +exprBot.toString(e, debug);
    }

    @Override
    public RestAction<Void> runRestAction(Event e) {
        User user = exprUser.getSingle(e);
        Guild guild = exprGuild.getSingle(e);
        final Bot bot = exprBot.getSingle(e);

        if (guild == null || user == null)
            return null;

        if (bot != null)
            guild = bot.getCore().getGuildById(guild.getId());

        return guild.unban(user.getId());
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprUser = (Expression<User>) exprs[0];
        exprGuild = (Expression<Guild>) exprs[1];
        exprBot = (Expression<Bot>) exprs[2];
        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);

        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }
}
