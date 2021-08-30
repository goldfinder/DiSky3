package info.itsthesky.disky3.core.skript;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.WaiterBotEffect;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import info.itsthesky.disky3.api.skript.events.MessageEvent;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class EffReplyWith extends WaiterBotEffect<UpdatingMessage> {

    static {
        register(
                EffReplyWith.class,
                "reply with [the] [message] %embedbuilder/string/messagebuilder% [and store (it|the message) in %-object%]"
        );
    }

    private Expression<Object> exprMessage;

    @Override
    @SuppressWarnings("unchecked")
    public boolean initEffect(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {

        if (!Arrays.asList(ScriptLoader.getCurrentEvents()[0].getInterfaces()).contains(MessageEvent.class)) {
            Skript.error("The reply effect can only be used in message event!");
            return false;
        }

        exprMessage = (Expression<Object>) exprs[0];

        final Variable<?> var = Utils.parseVar(exprs[1], false);
        setChangedVariable((Variable<UpdatingMessage>) var);

        return true;
    }

    @Override
    public void runEffect(Event e) {
        MessageBuilder message = Utils.parseMessageContent(Utils.verifyVar(e, exprMessage, null));
        if (message == null) return;

        MessageChannel channel;
        if (Utils.verifyVar(e, getUsedBot(), null) != null) {
            if (((MessageEvent) e).getMessageChannel() instanceof PrivateChannel) {
                channel = BotManager.specificSearch(getUsedBot().getSingle(e), bot -> bot.getCore().getPrivateChannelById(((MessageEvent) e).getMessageChannel().getId()));
            } else {
                channel = BotManager.specificSearch(getUsedBot().getSingle(e), bot -> bot.getCore().getTextChannelById(((MessageEvent) e).getMessageChannel().getId()));
            }
        } else {
            channel = ((MessageEvent) e).getMessageChannel();
        }

        if (channel == null) return;

        channel
                .sendMessage(message.build())
                .queue(msg -> restart(UpdatingMessage.from(msg)),
                        ex -> DiSky.exception(ex, getNode())
                );
    }

    @Override
    public String toStringEffect(@Nullable Event e, boolean debug) {
        return "reply with ";
    }
}
