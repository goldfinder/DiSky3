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
import net.dv8tion.jda.api.entities.ThreadChannel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Leave Thread")
@Description({"Leave a specific thread using a bot.",
        "This can throw an exception if the bot is already in that thread."})
public class LeaveThread extends WaiterEffect<Void> {

    static {
        Skript.registerEffect(
                LeaveThread.class,
                "leave [the] [thread] %thread% [(with|using) [bot] %-bot%]"
        );
    }

    private Expression<ThreadChannel> exprThread;
    private Expression<Bot> exprBot;

    @Override
    public boolean initEffect(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprThread = (Expression<ThreadChannel>) expressions[0];
        exprBot = (Expression<Bot>) expressions[1];
        if (exprBot == null)
            Utils.defaultToEventValue(exprBot, Bot.class);
        if (exprBot == null)
            Skript.error("The bot cannot be retrieved in the leave thread to effect!");
        return true;
    }

    @Override
    public void runEffect(Event e) {
        final ThreadChannel thread = exprThread.getSingle(e);
        final Bot bot = exprBot.getSingle(e);
        if (thread == null || bot == null)
        {
            restart();
            return;
        }

        bot
                .getCore()
                .getThreadChannelById(thread.getId())
                .leave()
                .queue(this::restart, ex -> DiSky.exception(ex, getNode()));
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "leave thread " + exprThread.toString(e, debug) + " using bot " + exprBot.toString(e, debug);
    }

}
