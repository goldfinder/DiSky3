package info.itsthesky.disky3.core.events.message;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.MessageEvent;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class PrivateReceive extends DiSkyEvent<PrivateMessageReceivedEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", PrivateReceive.class, EvtPrivateReceive.class,
                "(direct|private) message [receive]")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");

       EventValues.registerEventValue(EvtPrivateReceive.class, User.class, new Getter<User, EvtPrivateReceive>() {
            @Override
            public User get(EvtPrivateReceive event) {
                return event.getJDAEvent().getAuthor();
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReceive.class, UpdatingMessage.class, new Getter<UpdatingMessage, EvtPrivateReceive>() {
            @Override
            public UpdatingMessage get(EvtPrivateReceive event) {
                return UpdatingMessage.from(event.getJDAEvent().getChannel().retrieveMessageById(event.getJDAEvent().getMessageId()).complete());
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReceive.class, String.class, new Getter<String, EvtPrivateReceive>() {
            @Override
            public String get(EvtPrivateReceive event) {
                return event.getJDAEvent().getMessageId();
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReceive.class, PrivateChannel.class, new Getter<PrivateChannel, EvtPrivateReceive>() {
            @Override
            public PrivateChannel get(EvtPrivateReceive event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReceive.class, Bot.class, new Getter<Bot, EvtPrivateReceive>() {
            @Override
            public Bot get(EvtPrivateReceive event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtPrivateReceive extends SimpleDiSkyEvent<PrivateMessageReceivedEvent> implements MessageEvent {
        public EvtPrivateReceive(PrivateReceive event) { }

        @Override
        public MessageChannel getMessageChannel() {
            return getJDAEvent().getChannel();
        }
    }

}