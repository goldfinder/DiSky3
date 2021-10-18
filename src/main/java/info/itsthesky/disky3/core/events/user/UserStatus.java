package info.itsthesky.disky3.core.events.user;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import org.jetbrains.annotations.NotNull;

public class UserStatus extends DiSkyEvent<UserUpdateOnlineStatusEvent> {

    static {
        DiSkyEvent.register("User Status CHange", UserStatus.class, EvtUserStatus.class,
                "[discord] user [online] status (update|change)")
                .setName("User Status Change")
                .setDesc("Fired when a user update its online status.")
                .setExample("on user status change:");


       EventValues.registerEventValue(EvtUserStatus.class, Guild.class, new Getter<Guild, EvtUserStatus>() {
            @Override
            public Guild get(@NotNull EvtUserStatus event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtUserStatus.class, Member.class, new Getter<Member, EvtUserStatus>() {
            @Override
            public Member get(@NotNull EvtUserStatus event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

       EventValues.registerEventValue(EvtUserStatus.class, OnlineStatus.class, new Getter<OnlineStatus, EvtUserStatus>() {
            @Override
            public OnlineStatus get(@NotNull EvtUserStatus event) {
                return event.getJDAEvent().getOldOnlineStatus();
            }
        }, -1);

        EventValues.registerEventValue(EvtUserStatus.class, OnlineStatus.class, new Getter<OnlineStatus, EvtUserStatus>() {
            @Override
            public OnlineStatus get(@NotNull EvtUserStatus event) {
                return event.getJDAEvent().getNewOnlineStatus();
            }
        }, 1);

        EventValues.registerEventValue(EvtUserStatus.class, OnlineStatus.class, new Getter<OnlineStatus, EvtUserStatus>() {
            @Override
            public OnlineStatus get(@NotNull EvtUserStatus event) {
                return event.getJDAEvent().getNewOnlineStatus();
            }
        }, 0);

       EventValues.registerEventValue(EvtUserStatus.class, User.class, new Getter<User, EvtUserStatus>() {
            @Override
            public User get(@NotNull EvtUserStatus event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtUserStatus.class, Bot.class, new Getter<Bot, EvtUserStatus>() {
            @Override
            public Bot get(@NotNull EvtUserStatus event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtUserStatus extends SimpleDiSkyEvent<UserUpdateOnlineStatusEvent> {
        public EvtUserStatus(UserStatus event) { }
    }

}