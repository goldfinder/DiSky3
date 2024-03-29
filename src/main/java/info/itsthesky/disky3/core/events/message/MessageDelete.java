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
import org.jetbrains.annotations.NotNull;

public class MessageDelete extends DiSkyEvent<MessageDeleteEvent> {

    public static String content;
    public static Long id;

    static {
        DiSkyEvent.register("Message Delete", MessageDelete.class, EvtMessageDelete.class,
                "message delete[d]")
                .setName("Message Delete")
                .setDesc("Fired when a message is deleted, use event-string to get the message content.")
                .setExample("on message delete:");


        EventValues.registerEventValue(EvtMessageDelete.class, TextChannel.class, new Getter<TextChannel, EvtMessageDelete>() {
            @Override
            public TextChannel get(@NotNull EvtMessageDelete event) {
                return (TextChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageDelete.class, Guild.class, new Getter<Guild, EvtMessageDelete>() {
            @Override
            public Guild get(@NotNull EvtMessageDelete event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageDelete.class, Bot.class, new Getter<Bot, EvtMessageDelete>() {
            @Override
            public Bot get(@NotNull EvtMessageDelete event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageDelete.class, String.class, new Getter<String, EvtMessageDelete>() {
            @Override
            public String get(@NotNull EvtMessageDelete event) {
                return content;
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageDelete.class, Number.class, new Getter<Number, EvtMessageDelete>() {
            @Override
            public Number get(@NotNull EvtMessageDelete event) {
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