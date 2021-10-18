package info.itsthesky.disky3.core.events.role;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.role.update.RoleUpdateMentionableEvent;
import org.jetbrains.annotations.NotNull;

public class RoleMentionable extends DiSkyEvent<RoleUpdateMentionableEvent> {

    static {
        DiSkyEvent.register("Role Mentionable Change", RoleMentionable.class, EvtRoleMentionable.class,
                "[discord] [guild] role mentionable (update|change)")
                .setName("Role Mentionable Change")
                .setDesc("Fired when the mentionable stats of a role changes.")
                .setExample("on role mentionable change:");


        EventValues.registerEventValue(EvtRoleMentionable.class, Boolean.class, new Getter<Boolean, EvtRoleMentionable>() {
            @Override
            public Boolean get(@NotNull EvtRoleMentionable event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 1);

        EventValues.registerEventValue(EvtRoleMentionable.class, Boolean.class, new Getter<Boolean, EvtRoleMentionable>() {
            @Override
            public Boolean get(@NotNull EvtRoleMentionable event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleMentionable.class, Boolean.class, new Getter<Boolean, EvtRoleMentionable>() {
            @Override
            public Boolean get(@NotNull EvtRoleMentionable event) {
                return event.getJDAEvent().getOldValue();
            }
        }, -1);

       EventValues.registerEventValue(EvtRoleMentionable.class, Guild.class, new Getter<Guild, EvtRoleMentionable>() {
            @Override
            public Guild get(@NotNull EvtRoleMentionable event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleMentionable.class, Role.class, new Getter<Role, EvtRoleMentionable>() {
            @Override
            public Role get(@NotNull EvtRoleMentionable event) {
                return event.getJDAEvent().getRole();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleMentionable.class, Bot.class, new Getter<Bot, EvtRoleMentionable>() {
            @Override
            public Bot get(@NotNull EvtRoleMentionable event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtRoleMentionable extends SimpleDiSkyEvent<RoleUpdateMentionableEvent> implements LogEvent {
        public EvtRoleMentionable(RoleMentionable event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}