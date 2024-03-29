package info.itsthesky.disky3.core.events.role;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import org.jetbrains.annotations.NotNull;

public class RoleName extends DiSkyEvent<RoleUpdateNameEvent> {

    static {
        DiSkyEvent.register("Role Name Change", RoleName.class, EvtRoleName.class,
                "[discord] [guild] role name (update|change)")
                .setName("Role Name Change")
                .setDesc("Fired when the name of a role changes.")
                .setExample("on role name change:");

       EventValues.registerEventValue(EvtRoleName.class, String.class, new Getter<String, EvtRoleName>() {
            @Override
            public String get(@NotNull EvtRoleName event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 1);

       EventValues.registerEventValue(EvtRoleName.class, String.class, new Getter<String, EvtRoleName>() {
            @Override
            public String get(@NotNull EvtRoleName event) {
                return event.getJDAEvent().getOldName();
            }
        }, -1);

       EventValues.registerEventValue(EvtRoleName.class, String.class, new Getter<String, EvtRoleName>() {
            @Override
            public String get(@NotNull EvtRoleName event) {
                return event.getJDAEvent().getOldValue();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleName.class, Guild.class, new Getter<Guild, EvtRoleName>() {
            @Override
            public Guild get(@NotNull EvtRoleName event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleName.class, Role.class, new Getter<Role, EvtRoleName>() {
            @Override
            public Role get(@NotNull EvtRoleName event) {
                return event.getJDAEvent().getRole();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleName.class, Bot.class, new Getter<Bot, EvtRoleName>() {
            @Override
            public Bot get(@NotNull EvtRoleName event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtRoleName extends SimpleDiSkyEvent<RoleUpdateNameEvent> implements LogEvent {
        public EvtRoleName(RoleName event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}