package info.itsthesky.disky3.core.events.voice;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceDeafenEvent;

public class VoiceDeafen extends DiSkyEvent<GuildVoiceDeafenEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", VoiceDeafen.class, EvtVoiceDeafen.class,
                "[voice] member deafen")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtVoiceDeafen.class, Member.class, new Getter<Member, EvtVoiceDeafen>() {
            @Override
            public Member get(EvtVoiceDeafen event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceDeafen.class, Guild.class, new Getter<Guild, EvtVoiceDeafen>() {
            @Override
            public Guild get(EvtVoiceDeafen event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceDeafen.class, JDA.class, new Getter<JDA, EvtVoiceDeafen>() {
            @Override
            public JDA get(EvtVoiceDeafen event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtVoiceDeafen extends SimpleDiSkyEvent<GuildVoiceDeafenEvent> implements LogEvent {
        public EvtVoiceDeafen(VoiceDeafen event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}