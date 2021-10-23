package info.itsthesky.disky3.core.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.entities.GuildThread;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Join Thread")
@Description({"Join a specific thread using a bot.",
        "You must join a thread in order to send message in it, retrieve messages, etc...",
        "This can throw an exception if the bot is already in that thread or if he doesn't have the right permission."})
public class JoinThread extends WaiterEffect<Void> {

    static {
        Skript.registerEffect(
                JoinThread.class,
                "join [the] [thread] %thread% [(with|using) [bot] %-bot%]"
        );
    }

    private Expression<GuildThread> exprThread;
    private Expression<Bot> exprBot;

    @Override
    public boolean initEffect(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprThread = (Expression<GuildThread>) expressions[0];
        exprBot = (Expression<Bot>) expressions[1];
        if (exprBot == null)
            Utils.defaultToEventValue(exprBot, Bot.class);
        if (exprBot == null)
            Skript.error("The bot cannot be retrieved in the join thread to effect!");
        return true;
    }

    @Override
    public void runEffect(Event e) {
        final GuildThread thread = exprThread.getSingle(e);
        final Bot bot = Utils.verifyVar(e, exprBot, null);
        if (thread == null)
        {
            restart();
            return;
        }
        
        if (bot != null && !bot.getId().equalsIgnoreCase(thread.getJDA().getSelfUser().getId())) {
            bot
                    .getCore()
                    .getGuildThreadById(thread.getId())
                    .join()
                    .queue(this::restart, ex -> DiSky.exception(ex, getNode()));
        } else {
            thread
                    .join()
                    .queue(this::restart, ex -> DiSky.exception(ex, getNode()));
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "join thread " + exprThread.toString(e, debug) + " using bot " + exprBot.toString(e, debug);
    }

}
