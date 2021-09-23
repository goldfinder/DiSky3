package info.itsthesky.disky3.core.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.events.MultipleWaiterEffect;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EffRetrieveMessages extends MultipleWaiterEffect<UpdatingMessage> {

    static {
        Skript.registerEffect(
                EffRetrieveMessages.class,
                "retrieve [last] %number% [amount] message[s] (from|in|to) [channel] %textchannel/channel/user/member% [and store (them|the messages) in %-objects%] [(with|using) [the] [bot] %-bot%]"
        );
    }

    private Expression<Number> exprAmount;
    private Expression<Object> exprChannel;
    private Expression<Bot> exprBot;

    @Override
    @SuppressWarnings("unchecked")
    public boolean initEffect(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprAmount = (Expression<Number>) exprs[0];
        exprChannel = (Expression<Object>) exprs[1];
        exprBot = (Expression<Bot>) exprs[3];

        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);
        if (exprBot == null)
        {
            Skript.error("Unable to et the event bot in a retrieve messages effect");
            return false;
        }

        if (Utils.parseVar(exprs[2], true, true) == null)
            return false;

        setChangedVariable((Variable<UpdatingMessage>) exprs[2]);
        return true;
    }

    @Override
    public void runEffect(Event e) {
        Number amount = exprAmount.getSingle(e);
        Object target = exprChannel.getSingle(e);
        Bot bot = exprBot.getSingle(e);
        if (bot == null || target == null || amount == null)
            return;

        if (target instanceof User || target instanceof Member) {
            bot
                    .getCore()
                    .retrieveUserById(Utils.parseUser(target).getId())
                    .queue(user -> user
                            .openPrivateChannel()
                            .queue(cha -> cha
                                    .getHistory()
                                    .retrievePast(amount.intValue())
                                    .queue(msg -> restart(UpdatingMessage.convert(msg)))));
        } else {

            bot
                    .getCore()
                    .getTextChannelById(((GuildChannel) target).getId())
                    .getHistory()
                    .retrievePast(amount.intValue())
                    .queue(msg -> restart(UpdatingMessage.convert(msg)));

        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "retrieve last " + exprAmount.toString(e, debug) + " messages from channel " + exprChannel.toString(e, debug) + " using bot " + exprBot.toString(e, debug);
    }

}
