package info.itsthesky.disky3.core.skript.builder;

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
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.entities.BaseGuildMessageChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildThread;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Create Thread")
@Description({"Create a new thread in a text channel with a base name.",
        "The bot used in that effect will automatically join the thread, so you don't have to make it join yourself.",
        "If you create a private thread, then you cannot specify a message. ",
        "Else, the Thread will be created based on the specified message.",
        "Creating private thread need the guild to be level 2 or more, else it'll throw an exception."})
public class CreateThread extends WaiterEffect<GuildThread> {

    static {
        Skript.registerEffect(
                CreateThread.class,
                "(make|create) [the] [new] [private] thread (named|with name) %string% in [the] [channel] %channel/textchannel% [(with|using) [the] [message] [as reference] %-message%] [(with|using) [the] [bot] %-bot%] and store (it|the role) in %object%"
        );
    }

    private Expression<String> exprName;
    private Expression<UpdatingMessage> exprMessage;
    private Expression<BaseGuildMessageChannel> exprChannel;
    private Expression<Bot> exprBot;
    private boolean isPrivate = false;

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "create new role named " + exprName.toString(e, debug) + " using bot " + exprBot.toString(e, debug);
    }

    @Override
    public boolean initEffect(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprName = (Expression<String>) exprs[0];
        exprChannel = (Expression<BaseGuildMessageChannel>) exprs[1];
        exprMessage = (Expression<UpdatingMessage>) exprs[2];
        exprBot = Utils.verifyDefaultToEvent(exprs[3], exprBot, Bot.class);
        setChangedVariable((Variable<GuildThread>) exprs[4]);

        isPrivate = parseResult.expr.contains("private thread");

        if (exprBot == null)
        {
            DiSky.exception(new DiSkyException("The bot cannot be retrieved in the create thread effect!"), getNode());
            return false;
        }

        return true;
    }

    @Override
    public void runEffect(Event e) {
        final String name = exprName.getSingle(e);
        final Bot bot = exprBot.getSingle(e);
        final @Nullable UpdatingMessage message = Utils.verifyVar(e, exprMessage, null);
        BaseGuildMessageChannel channel = exprChannel.getSingle(e);
        if (name == null || bot == null || channel == null) {
            restart();
            return;
        }
        channel = bot.getCore().getTextChannelById(channel.getId());

        final RestAction<GuildThread> action;
        if (isPrivate) {
            action = channel.createThread(name, isPrivate);
        } else {
            if (message == null) {
                action = channel.createThread(name);
            } else {
                action = channel.createThread(name, message.getMessage().getId());
            }
        }
        action.queue(this::restart);
    }
}
