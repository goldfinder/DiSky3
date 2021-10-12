package info.itsthesky.disky3.core.events.message;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.specific.MessageEvent;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import org.jetbrains.annotations.NotNull;

public class ReactionClear extends DiSkyEvent<MessageReactionRemoveAllEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", ReactionClear.class, EvtReactionClear.class,
                "reaction (remove all|clear)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtReactionClear.class, TextChannel.class, new Getter<TextChannel, EvtReactionClear>() {
            @Override
            public TextChannel get(@NotNull EvtReactionClear event) {
                return (TextChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtReactionClear.class, Guild.class, new Getter<Guild, EvtReactionClear>() {
            @Override
            public Guild get(@NotNull EvtReactionClear event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtReactionClear.class, Bot.class, new Getter<Bot, EvtReactionClear>() {
            @Override
            public Bot get(@NotNull EvtReactionClear event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

        EventValues.registerEventValue(EvtReactionClear.class, UpdatingMessage.class, new Getter<UpdatingMessage, EvtReactionClear>() {
            @Override
            public UpdatingMessage get(@NotNull EvtReactionClear event) {
                return UpdatingMessage.from(event.getJDAEvent().getChannel().retrieveMessageById(event.getJDAEvent().getMessageId()).complete());
            }
        }, 0);

    }

    public static class EvtReactionClear extends SimpleDiSkyEvent<MessageReactionRemoveAllEvent> implements MessageEvent {
        public EvtReactionClear(ReactionClear event) { }

        @Override
        public MessageChannel getMessageChannel() {
            return getJDAEvent().getChannel();
        }
    }

}