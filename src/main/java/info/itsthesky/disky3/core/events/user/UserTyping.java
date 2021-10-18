package info.itsthesky.disky3.core.events.user;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Date;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.user.UserTypingEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateAvatarEvent;
import org.jetbrains.annotations.NotNull;

public class UserTyping extends DiSkyEvent<UserTypingEvent> {

    static {
        DiSkyEvent.register("User Typing", UserTyping.class, EvtUserAvatar.class,
                "[discord] user typing [in channel]")
                .setName("User Typing")
                .setDesc("Fired when a user start typing in a channel.")
                .setExample("on user typing:");

        EventValues.registerEventValue(EvtUserAvatar.class, User.class, new Getter<User, EvtUserAvatar>() {
            @Override
            public User get(@NotNull EvtUserAvatar event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

        EventValues.registerEventValue(EvtUserAvatar.class, Member.class, new Getter<Member, EvtUserAvatar>() {
            @Override
            public Member get(@NotNull EvtUserAvatar event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EvtUserAvatar.class, Guild.class, new Getter<Guild, EvtUserAvatar>() {
            @Override
            public Guild get(@NotNull EvtUserAvatar event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtUserAvatar.class, TextChannel.class, new Getter<TextChannel, EvtUserAvatar>() {
            @Override
            public TextChannel get(@NotNull EvtUserAvatar event) {
                return event.getJDAEvent().getTextChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtUserAvatar.class, Date.class, new Getter<Date, EvtUserAvatar>() {
            @Override
            public Date get(@NotNull EvtUserAvatar event) {
                return Utils.convert(event.getJDAEvent().getTimestamp());
            }
        }, 0);

       EventValues.registerEventValue(EvtUserAvatar.class, Bot.class, new Getter<Bot, EvtUserAvatar>() {
            @Override
            public Bot get(@NotNull EvtUserAvatar event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtUserAvatar extends SimpleDiSkyEvent<UserTypingEvent> {
        public EvtUserAvatar(UserTyping event) { }
    }

}