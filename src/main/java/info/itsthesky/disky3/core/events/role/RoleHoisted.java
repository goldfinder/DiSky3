package info.itsthesky.disky3.core.events.role;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.role.update.RoleUpdateHoistedEvent;

public class RoleHoisted extends DiSkyEvent<RoleUpdateHoistedEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", RoleHoisted.class, EvtRoleHoisted.class,
                "[discord] role hoist[ed] (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


        EventValues.registerEventValue(EvtRoleHoisted.class, Boolean.class, new Getter<Boolean, EvtRoleHoisted>() {
            @Override
            public Boolean get(EvtRoleHoisted event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 1);

        EventValues.registerEventValue(EvtRoleHoisted.class, Boolean.class, new Getter<Boolean, EvtRoleHoisted>() {
            @Override
            public Boolean get(EvtRoleHoisted event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleHoisted.class, Boolean.class, new Getter<Boolean, EvtRoleHoisted>() {
            @Override
            public Boolean get(EvtRoleHoisted event) {
                return event.getJDAEvent().getOldValue();
            }
        }, -1);

       EventValues.registerEventValue(EvtRoleHoisted.class, Guild.class, new Getter<Guild, EvtRoleHoisted>() {
            @Override
            public Guild get(EvtRoleHoisted event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleHoisted.class, Role.class, new Getter<Role, EvtRoleHoisted>() {
            @Override
            public Role get(EvtRoleHoisted event) {
                return event.getJDAEvent().getRole();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleHoisted.class, Bot.class, new Getter<Bot, EvtRoleHoisted>() {
            @Override
            public Bot get(EvtRoleHoisted event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtRoleHoisted extends SimpleDiSkyEvent<RoleUpdateHoistedEvent> implements LogEvent {
        public EvtRoleHoisted(RoleHoisted event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}