package info.itsthesky.disky3.core.events.user;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.user.update.UserUpdateAvatarEvent;
import org.jetbrains.annotations.NotNull;

public class UserAvatar extends DiSkyEvent<UserUpdateAvatarEvent> {

    static {
        DiSkyEvent.register("User Avatar Change", UserAvatar.class, EvtUserAvatar.class,
                "[discord] user avatar (update|change)")
                .setName("User Avatar Change")
                .setDesc("Fired when a user change its avatar.")
                .setExample("on user avatar change:");

        EventValues.registerEventValue(EvtUserAvatar.class, String.class, new Getter<String, EvtUserAvatar>() {
            @Override
            public String get(@NotNull EvtUserAvatar event) {
                return event.getJDAEvent().getOldAvatarUrl();
            }
        }, -1);

        EventValues.registerEventValue(EvtUserAvatar.class, String.class, new Getter<String, EvtUserAvatar>() {
            @Override
            public String get(@NotNull EvtUserAvatar event) {
                return event.getJDAEvent().getOldAvatarUrl();
            }
        }, 0);

       EventValues.registerEventValue(EvtUserAvatar.class, String.class, new Getter<String, EvtUserAvatar>() {
            @Override
            public String get(@NotNull EvtUserAvatar event) {
                return event.getJDAEvent().getNewAvatarUrl();
            }
        }, 1);

       EventValues.registerEventValue(EvtUserAvatar.class, User.class, new Getter<User, EvtUserAvatar>() {
            @Override
            public User get(@NotNull EvtUserAvatar event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtUserAvatar.class, Bot.class, new Getter<Bot, EvtUserAvatar>() {
            @Override
            public Bot get(@NotNull EvtUserAvatar event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtUserAvatar extends SimpleDiSkyEvent<UserUpdateAvatarEvent> {
        public EvtUserAvatar(UserAvatar event) { }
    }

}