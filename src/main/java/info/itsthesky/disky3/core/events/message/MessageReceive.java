package info.itsthesky.disky3.core.events.message;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.MessageEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MessageReceive extends DiSkyEvent<MessageReceivedEvent> {

    static {
        DiSkyEvent.register("MessageReceive", MessageReceive.class, EvtGuildMessage.class,
                "[guild] message receive")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


        EventValues.registerEventValue(EvtGuildMessage.class, UpdatingMessage.class, new Getter<UpdatingMessage, EvtGuildMessage>() {
            @Override
            public UpdatingMessage get(EvtGuildMessage event) {
                return UpdatingMessage.from(event.getJDAEvent().getMessage());
            }
        }, 0);

        EventValues.registerEventValue(EvtGuildMessage.class, User.class, new Getter<User, EvtGuildMessage>() {
            @Override
            public User get(EvtGuildMessage event) {
                return event.getJDAEvent().getAuthor();
            }
        }, 0);

        EventValues.registerEventValue(EvtGuildMessage.class, Member.class, new Getter<Member, EvtGuildMessage>() {
            @Override
            public Member get(EvtGuildMessage event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EvtGuildMessage.class, TextChannel.class, new Getter<TextChannel, EvtGuildMessage>() {
            @Override
            public TextChannel get(EvtGuildMessage event) {
                return (TextChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtGuildMessage.class, GuildChannel.class, new Getter<GuildChannel, EvtGuildMessage>() {
            @Override
            public GuildChannel get(EvtGuildMessage event) {
                return (GuildChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtGuildMessage.class, Guild.class, new Getter<Guild, EvtGuildMessage>() {
            @Override
            public Guild get(EvtGuildMessage event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtGuildMessage.class, Bot.class, new Getter<Bot, EvtGuildMessage>() {
            @Override
            public Bot get(EvtGuildMessage event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtGuildMessage extends SimpleDiSkyEvent<MessageReceivedEvent> implements MessageEvent {
        public EvtGuildMessage(MessageReceive event) { }

        @Override
        public MessageChannel getMessageChannel() {
            return getJDAEvent().getChannel();
        }
    }

}