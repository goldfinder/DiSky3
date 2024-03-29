package info.itsthesky.disky3.core.events.message;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.MessageEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class PrivateReceive extends DiSkyEvent<MessageReceivedEvent> {

    @Override
    protected Predicate<MessageReceivedEvent> checker() {
        return e -> !e.isFromGuild();
    }

    static {
        DiSkyEvent.register("Private Message Receive", PrivateReceive.class, EvtPrivateReceive.class,
                "(direct|private) message [receive]")
                .setName("Private Message Receive")
                .setDesc("Fired when a private message is received.")
                .setExample("on private message receive:");

       EventValues.registerEventValue(EvtPrivateReceive.class, User.class, new Getter<User, EvtPrivateReceive>() {
            @Override
            public User get(@NotNull EvtPrivateReceive event) {
                return event.getJDAEvent().getAuthor();
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReceive.class, UpdatingMessage.class, new Getter<UpdatingMessage, EvtPrivateReceive>() {
            @Override
            public UpdatingMessage get(@NotNull EvtPrivateReceive event) {
                return UpdatingMessage.from(event.getJDAEvent().getChannel().retrieveMessageById(event.getJDAEvent().getMessageId()).complete());
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReceive.class, String.class, new Getter<String, EvtPrivateReceive>() {
            @Override
            public String get(@NotNull EvtPrivateReceive event) {
                return event.getJDAEvent().getMessageId();
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReceive.class, PrivateChannel.class, new Getter<PrivateChannel, EvtPrivateReceive>() {
            @Override
            public PrivateChannel get(@NotNull EvtPrivateReceive event) {
                return (PrivateChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReceive.class, Bot.class, new Getter<Bot, EvtPrivateReceive>() {
            @Override
            public Bot get(@NotNull EvtPrivateReceive event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtPrivateReceive extends SimpleDiSkyEvent<MessageReceivedEvent> implements MessageEvent {
        public EvtPrivateReceive(PrivateReceive event) { }

        @Override
        public MessageChannel getMessageChannel() {
            return getJDAEvent().getChannel();
        }
    }

}