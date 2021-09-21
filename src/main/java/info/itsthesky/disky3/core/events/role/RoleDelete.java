package info.itsthesky.disky3.core.events.role;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;

public class RoleDelete extends DiSkyEvent<RoleDeleteEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", RoleDelete.class, EvtRoleDelete.class,
                "[guild] role delete")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtRoleDelete.class, Role.class, new Getter<Role, EvtRoleDelete>() {
            @Override
            public Role get(EvtRoleDelete event) {
                return event.getJDAEvent().getRole();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleDelete.class, Guild.class, new Getter<Guild, EvtRoleDelete>() {
            @Override
            public Guild get(EvtRoleDelete event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleDelete.class, Bot.class, new Getter<Bot, EvtRoleDelete>() {
            @Override
            public Bot get(EvtRoleDelete event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtRoleDelete extends SimpleDiSkyEvent<RoleDeleteEvent> implements LogEvent {
        public EvtRoleDelete(RoleDelete event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}