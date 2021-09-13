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
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RetrieveInvites extends WaiterEffect<Invite[]> {

    static {
        Skript.registerEffect(
                RetrieveInvites.class,
                "retrieve [the] invite[s] (from|in|of) [the] [guild] %guild% [with [the] [bot] %-bot%] and store (them|the invites) in %objects%"
        );
    }

    private Expression<Bot> exprBot;
    private Expression<Guild> exprGuild;

    @Override
    public boolean initEffect(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprGuild = (Expression<Guild>) exprs[0];
        exprBot = (Expression<Bot>) exprs[1];
        setChangedVariable((Variable<Invite[]>) exprs[2]);

        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);

        if (exprBot == null) {
            DiSky.exception(new DiSkyException("The bot cannot be found in a retrieve invites effect!"), getNode());
            return false;
        }

        return true;
    }

    @Override
    public void runEffect(Event e) {
        Bot bot = exprBot.getSingle(e);
        Guild guild = exprGuild.getSingle(e);
        if (bot == null || guild == null) return;

        bot.getCore().getGuildById(guild.getId()).retrieveInvites().queue(invites -> restart(invites.toArray(new Invite[0])), ex -> DiSky.exception(ex, getNode()));
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "retrieve invites of guild " + exprGuild.toString(e, debug);
    }
}
