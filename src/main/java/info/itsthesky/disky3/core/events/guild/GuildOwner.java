package info.itsthesky.disky3.core.events.guild;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateOwnerEvent;

public class GuildOwner extends DiSkyEvent<GuildUpdateOwnerEvent> {

    static {
        DiSkyEvent.register("Guild Owner Update", GuildOwner.class, EvtGuildOwner.class,
                "[guild] owner (change|update)")
                .setName("Guild Owner Update");


       EventValues.registerEventValue(EvtGuildOwner.class, Member.class, new Getter<Member, EvtGuildOwner>() {
            @Override
            public Member get(EvtGuildOwner event) {
                return event.getJDAEvent().getOldOwner();
            }
        }, -1);

        EventValues.registerEventValue(EvtGuildOwner.class, Member.class, new Getter<Member, EvtGuildOwner>() {
            @Override
            public Member get(EvtGuildOwner event) {
                return event.getJDAEvent().getNewOwner();
            }
        }, 1);

        EventValues.registerEventValue(EvtGuildOwner.class, Member.class, new Getter<Member, EvtGuildOwner>() {
            @Override
            public Member get(EvtGuildOwner event) {
                return event.getJDAEvent().getNewOwner();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildOwner.class, Guild.class, new Getter<Guild, EvtGuildOwner>() {
            @Override
            public Guild get(EvtGuildOwner event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildOwner.class, Bot.class, new Getter<Bot, EvtGuildOwner>() {
            @Override
            public Bot get(EvtGuildOwner event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtGuildOwner extends SimpleDiSkyEvent<GuildUpdateOwnerEvent> implements LogEvent {
        public EvtGuildOwner(GuildOwner event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}