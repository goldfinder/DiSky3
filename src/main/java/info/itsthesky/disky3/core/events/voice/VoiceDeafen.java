package info.itsthesky.disky3.core.events.voice;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceDeafenEvent;
import org.jetbrains.annotations.NotNull;

public class VoiceDeafen extends DiSkyEvent<GuildVoiceDeafenEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", VoiceDeafen.class, EvtVoiceDeafen.class,
                "[voice] member deafen")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtVoiceDeafen.class, Member.class, new Getter<Member, EvtVoiceDeafen>() {
            @Override
            public Member get(@NotNull EvtVoiceDeafen event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceDeafen.class, Guild.class, new Getter<Guild, EvtVoiceDeafen>() {
            @Override
            public Guild get(@NotNull EvtVoiceDeafen event) {
                return ((VoiceChannel) event.getJDAEvent().getVoiceState().getChannel()).getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceDeafen.class, Bot.class, new Getter<Bot, EvtVoiceDeafen>() {
            @Override
            public Bot get(@NotNull EvtVoiceDeafen event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
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