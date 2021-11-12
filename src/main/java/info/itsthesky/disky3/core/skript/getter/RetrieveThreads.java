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
import info.itsthesky.disky3.api.skript.events.MultipleWaiterEffect;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ThreadChannel;
import net.dv8tion.jda.api.entities.Invite;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Retrieve Threads")
@Description({"Retrieve every threads (and cache them) from a specific guild.",
"This effect will only get back the ACTIVE thread, and will pass on the archived ones."})
public class RetrieveThreads extends MultipleWaiterEffect<ThreadChannel> {

    static {
        Skript.registerEffect(
                RetrieveThreads.class,
                "retrieve [the] thread[s] (from|in|of) [the] [guild] %guild% [with [the] [bot] %-bot%] and store (them|the invites) in %objects%"
        );
    }

    private Expression<Bot> exprBot;
    private Expression<Guild> exprGuild;

    @Override
    public boolean initEffect(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprGuild = (Expression<Guild>) exprs[0];
        exprBot = (Expression<Bot>) exprs[1];
        setChangedVariable((Variable<ThreadChannel>) exprs[2]);

        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);

        if (exprBot == null) {
            DiSky.exception(new DiSkyException("The bot cannot be found in a retrieve threads effect!"), getNode());
            return false;
        }

        return true;
    }

    @Override
    public void runEffect(Event e) {
        Bot bot = exprBot.getSingle(e);
        Guild guild = exprGuild.getSingle(e);
        if (bot == null || guild == null) return;

        bot.getCore().getGuildById(guild.getId()).retrieveActiveThreads().queue(threads -> restart(threads.toArray(new ThreadChannel[0])), ex -> DiSky.exception(ex, getNode()));
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "retrieve threads of guild " + exprGuild.toString(e, debug);
    }
}
