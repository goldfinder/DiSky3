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
import org.jetbrains.annotations.NotNull;

public class MessageReceive extends DiSkyEvent<MessageReceivedEvent> {

    @Override
    public boolean check(MessageReceivedEvent event) {
        return event.isFromGuild();
    }

    static {
        DiSkyEvent.register("Message Receive", MessageReceive.class, EvtGuildMessage.class,
                "[guild] message receive[d]")
                .setName("Message Receive")
                .setDesc("Fired when a message is received.")
                .setExample("on message receive:");


        EventValues.registerEventValue(EvtGuildMessage.class, UpdatingMessage.class, new Getter<UpdatingMessage, EvtGuildMessage>() {
            @Override
            public UpdatingMessage get(@NotNull EvtGuildMessage event) {
                return UpdatingMessage.from(event.getJDAEvent().getMessage());
            }
        }, 0);

        EventValues.registerEventValue(EvtGuildMessage.class, User.class, new Getter<User, EvtGuildMessage>() {
            @Override
            public User get(@NotNull EvtGuildMessage event) {
                return event.getJDAEvent().getAuthor();
            }
        }, 0);

        EventValues.registerEventValue(EvtGuildMessage.class, Member.class, new Getter<Member, EvtGuildMessage>() {
            @Override
            public Member get(@NotNull EvtGuildMessage event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EvtGuildMessage.class, TextChannel.class, new Getter<TextChannel, EvtGuildMessage>() {
            @Override
            public TextChannel get(@NotNull EvtGuildMessage event) {
                return (TextChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtGuildMessage.class, GuildChannel.class, new Getter<GuildChannel, EvtGuildMessage>() {
            @Override
            public GuildChannel get(@NotNull EvtGuildMessage event) {
                return (GuildChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtGuildMessage.class, Guild.class, new Getter<Guild, EvtGuildMessage>() {
            @Override
            public Guild get(@NotNull EvtGuildMessage event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtGuildMessage.class, Bot.class, new Getter<Bot, EvtGuildMessage>() {
            @Override
            public Bot get(@NotNull EvtGuildMessage event) {
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