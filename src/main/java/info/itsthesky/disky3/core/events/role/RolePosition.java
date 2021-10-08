package info.itsthesky.disky3.core.events.role;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.role.update.RoleUpdatePositionEvent;

public class RolePosition extends DiSkyEvent<RoleUpdatePositionEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", RolePosition.class, EvtRolePosition.class,
                "[discord] role position (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtRolePosition.class, Integer.class, new Getter<Integer, EvtRolePosition>() {
            @Override
            public Integer get(EvtRolePosition event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 1);

        EventValues.registerEventValue(EvtRolePosition.class, Integer.class, new Getter<Integer, EvtRolePosition>() {
            @Override
            public Integer get(EvtRolePosition event) {
                return event.getJDAEvent().getOldValue();
            }
        }, -1);

        EventValues.registerEventValue(EvtRolePosition.class, Integer.class, new Getter<Integer, EvtRolePosition>() {
            @Override
            public Integer get(EvtRolePosition event) {
                return event.getJDAEvent().getOldValue();
            }
        }, 0);

       EventValues.registerEventValue(EvtRolePosition.class, Guild.class, new Getter<Guild, EvtRolePosition>() {
            @Override
            public Guild get(EvtRolePosition event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtRolePosition.class, Role.class, new Getter<Role, EvtRolePosition>() {
            @Override
            public Role get(EvtRolePosition event) {
                return event.getJDAEvent().getRole();
            }
        }, 0);

       EventValues.registerEventValue(EvtRolePosition.class, Bot.class, new Getter<Bot, EvtRolePosition>() {
            @Override
            public Bot get(EvtRolePosition event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtRolePosition extends SimpleDiSkyEvent<RoleUpdatePositionEvent> implements LogEvent {
        public EvtRolePosition(RolePosition event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}