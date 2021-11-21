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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RetrieveMember extends WaiterEffect<Member> {

    static {
        Skript.registerEffect(
                RetrieveMember.class,
                "retrieve [the] member (from|with) id %string% [in [the] [guild] %-guild%] [with [the] [bot] %-bot%] and store (it|the user) in %object%"
        );
    }

    private Expression<String> exprID;
    private Expression<Bot> exprBot;
    private Expression<Guild> exprGuild;

    @Override
    public boolean initEffect(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprID = (Expression<String>) exprs[0];
        exprGuild = (Expression<Guild>) exprs[1];
        exprBot = (Expression<Bot>) exprs[2];
        setChangedVariable((Variable<Member>) exprs[3]);

        if (exprGuild == null)
            exprGuild = Utils.defaultToEventValue(exprGuild, Guild.class);

        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);

        if (exprBot == null || exprGuild == null) {
            DiSky.exception(new DiSkyException("The bot or the guild cannot be found in a retrieve member effect!"), getNode());
            return false;
        }

        return true;
    }

    @Override
    public void runEffect(Event e) {
        Bot bot = exprBot.getSingle(e);
        String id = exprID.getSingle(e);
        Guild guild = exprGuild.getSingle(e);
        if (id == null || bot == null || guild == null) return;

        bot.getCore().getGuildById(guild.getId()).retrieveMemberById(id).queue(this::restart, ex -> {
            DiSky.exception(ex, getNode());
            restart();
        });
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "retrieve member with id " + exprID.toString(e, debug);
    }
}
