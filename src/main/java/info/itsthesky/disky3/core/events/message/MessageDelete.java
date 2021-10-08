package info.itsthesky.disky3.core.events.message;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.specific.MessageEvent;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;

public class MessageDelete extends DiSkyEvent<MessageDeleteEvent> {

    public static String content;
    public static Long id;

    static {
        DiSkyEvent.register("Inner Event Name", MessageDelete.class, EvtMessageDelete.class,
                "message delete")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


        EventValues.registerEventValue(EvtMessageDelete.class, TextChannel.class, new Getter<TextChannel, EvtMessageDelete>() {
            @Override
            public TextChannel get(EvtMessageDelete event) {
                return (TextChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageDelete.class, Guild.class, new Getter<Guild, EvtMessageDelete>() {
            @Override
            public Guild get(EvtMessageDelete event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageDelete.class, Bot.class, new Getter<Bot, EvtMessageDelete>() {
            @Override
            public Bot get(EvtMessageDelete event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageDelete.class, String.class, new Getter<String, EvtMessageDelete>() {
            @Override
            public String get(EvtMessageDelete event) {
                return content;
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageDelete.class, Number.class, new Getter<Number, EvtMessageDelete>() {
            @Override
            public Number get(EvtMessageDelete event) {
                return id;
            }
        }, 0);

    }

    public static class EvtMessageDelete extends SimpleDiSkyEvent<MessageDeleteEvent> implements MessageEvent {
        public EvtMessageDelete(MessageDelete event) { }

        @Override
        public MessageChannel getMessageChannel() {
            return getJDAEvent().getChannel();
        }
    }

}