package info.itsthesky.disky3.core.events.guild;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateAfkChannelEvent;

public class GuildAFKChannel extends DiSkyEvent<GuildUpdateAfkChannelEvent> {

    static {
        DiSkyEvent.register("AFK Channel Update", GuildAFKChannel.class, EvtGuildAFKChannel.class,
                "[guild] afk channel (update|change)")
                .setName("AFK Channel Update");


       EventValues.registerEventValue(EvtGuildAFKChannel.class, VoiceChannel.class, new Getter<VoiceChannel, EvtGuildAFKChannel>() {
            @Override
            public VoiceChannel get(EvtGuildAFKChannel event) {
                return event.getJDAEvent().getOldAfkChannel();
            }
        }, -1);

        EventValues.registerEventValue(EvtGuildAFKChannel.class, VoiceChannel.class, new Getter<VoiceChannel, EvtGuildAFKChannel>() {
            @Override
            public VoiceChannel get(EvtGuildAFKChannel event) {
                return event.getJDAEvent().getNewAfkChannel();
            }
        }, 1);

        EventValues.registerEventValue(EvtGuildAFKChannel.class, VoiceChannel.class, new Getter<VoiceChannel, EvtGuildAFKChannel>() {
            @Override
            public VoiceChannel get(EvtGuildAFKChannel event) {
                return event.getJDAEvent().getNewAfkChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildAFKChannel.class, Guild.class, new Getter<Guild, EvtGuildAFKChannel>() {
            @Override
            public Guild get(EvtGuildAFKChannel event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildAFKChannel.class, JDA.class, new Getter<JDA, EvtGuildAFKChannel>() {
            @Override
            public JDA get(EvtGuildAFKChannel event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtGuildAFKChannel extends SimpleDiSkyEvent<GuildUpdateAfkChannelEvent> implements LogEvent {
        public EvtGuildAFKChannel(GuildAFKChannel event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}