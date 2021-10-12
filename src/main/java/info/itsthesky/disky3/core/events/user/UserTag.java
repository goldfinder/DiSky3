package info.itsthesky.disky3.core.events.user;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.user.update.UserUpdateDiscriminatorEvent;
import org.jetbrains.annotations.NotNull;

public class UserTag extends DiSkyEvent<UserUpdateDiscriminatorEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", UserTag.class, EvtUserTag.class,
                "[discord] user (tag|discriminator) (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


        EventValues.registerEventValue(EvtUserTag.class, String.class, new Getter<String, EvtUserTag>() {
            @Override
            public String get(@NotNull EvtUserTag event) {
                return event.getJDAEvent().getOldDiscriminator();
            }
        }, -1);

        EventValues.registerEventValue(EvtUserTag.class, String.class, new Getter<String, EvtUserTag>() {
            @Override
            public String get(@NotNull EvtUserTag event) {
                return event.getJDAEvent().getOldDiscriminator();
            }
        }, 0);

       EventValues.registerEventValue(EvtUserTag.class, String.class, new Getter<String, EvtUserTag>() {
            @Override
            public String get(@NotNull EvtUserTag event) {
                return event.getJDAEvent().getNewDiscriminator();
            }
        }, 1);

       EventValues.registerEventValue(EvtUserTag.class, User.class, new Getter<User, EvtUserTag>() {
            @Override
            public User get(@NotNull EvtUserTag event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtUserTag.class, Bot.class, new Getter<Bot, EvtUserTag>() {
            @Override
            public Bot get(@NotNull EvtUserTag event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtUserTag extends SimpleDiSkyEvent<UserUpdateDiscriminatorEvent> {
        public EvtUserTag(UserTag event) { }
    }

}