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
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import org.jetbrains.annotations.NotNull;

public class MessageEdit extends DiSkyEvent<MessageUpdateEvent> {

    public static String oldContent = "";
    public static String newContent = "";

    static {
        DiSkyEvent.register("Inner Event Name", MessageEdit.class, EvtMessageEdit.class,
                "message edit[ed]")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");

        EventValues.registerEventValue(EvtMessageEdit.class, UpdatingMessage.class, new Getter<UpdatingMessage, EvtMessageEdit>() {
            @Override
            public UpdatingMessage get(@NotNull EvtMessageEdit event) {
                return UpdatingMessage.from(event.getJDAEvent().getMessage());            }
        }, 0);

        EventValues.registerEventValue(EvtMessageEdit.class, User.class, new Getter<User, EvtMessageEdit>() {
            @Override
            public User get(@NotNull EvtMessageEdit event) {
                return event.getJDAEvent().getAuthor();
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageEdit.class, String.class, new Getter<String, EvtMessageEdit>() {
            @Override
            public String get(@NotNull EvtMessageEdit event) {
                return newContent;
            }
        }, 1);

        EventValues.registerEventValue(EvtMessageEdit.class, String.class, new Getter<String, EvtMessageEdit>() {
            @Override
            public String get(@NotNull EvtMessageEdit event) {
                return newContent;
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageEdit.class, String.class, new Getter<String, EvtMessageEdit>() {
            @Override
            public String get(@NotNull EvtMessageEdit event) {
                return oldContent;
            }
        }, -1);

        EventValues.registerEventValue(EvtMessageEdit.class, Member.class, new Getter<Member, EvtMessageEdit>() {
            @Override
            public Member get(@NotNull EvtMessageEdit event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageEdit.class, TextChannel.class, new Getter<TextChannel, EvtMessageEdit>() {
            @Override
            public TextChannel get(@NotNull EvtMessageEdit event) {
                return (TextChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageEdit.class, Guild.class, new Getter<Guild, EvtMessageEdit>() {
            @Override
            public Guild get(@NotNull EvtMessageEdit event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageEdit.class, Bot.class, new Getter<Bot, EvtMessageEdit>() {
            @Override
            public Bot get(@NotNull EvtMessageEdit event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtMessageEdit extends SimpleDiSkyEvent<MessageUpdateEvent> implements MessageEvent {
        public EvtMessageEdit(MessageEdit event) { }

        @Override
        public MessageChannel getMessageChannel() {
            return getJDAEvent().getChannel();
        }
    }

}