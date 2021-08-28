package info.itsthesky.disky3.core.events.guild;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;

public class GuildName extends DiSkyEvent<GuildUpdateNameEvent> {

    static {
        DiSkyEvent.register("Guild Name Update", GuildName.class, EvtGuildName.class,
                "guild name (change|update)")
                .setName("Guild Name Update");


        EventValues.registerEventValue(EvtGuildName.class, String.class, new Getter<String, EvtGuildName>() {
            @Override
            public String get(EvtGuildName event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 1);

        EventValues.registerEventValue(EvtGuildName.class, String.class, new Getter<String, EvtGuildName>() {
            @Override
            public String get(EvtGuildName event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 0);

        EventValues.registerEventValue(EvtGuildName.class, String.class, new Getter<String, EvtGuildName>() {
            @Override
            public String get(EvtGuildName event) {
                return event.getJDAEvent().getOldName();
            }
        }, -1);

        EventValues.registerEventValue(EvtGuildName.class, String.class, new Getter<String, EvtGuildName>() {
            @Override
            public String get(EvtGuildName event) {
                return event.getJDAEvent().getOldName();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildName.class, Guild.class, new Getter<Guild, EvtGuildName>() {
            @Override
            public Guild get(EvtGuildName event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildName.class, Guild.class, new Getter<Guild, EvtGuildName>() {
            @Override
            public Guild get(EvtGuildName event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildName.class, JDA.class, new Getter<JDA, EvtGuildName>() {
            @Override
            public JDA get(EvtGuildName event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtGuildName extends SimpleDiSkyEvent<GuildUpdateNameEvent> implements LogEvent {
        public EvtGuildName(GuildName event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}