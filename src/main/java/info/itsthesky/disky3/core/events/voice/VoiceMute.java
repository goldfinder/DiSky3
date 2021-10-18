package info.itsthesky.disky3.core.events.voice;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMuteEvent;
import org.jetbrains.annotations.NotNull;

public class VoiceMute extends DiSkyEvent<GuildVoiceMuteEvent> {

    static {
        DiSkyEvent.register("Voice Member Mute", VoiceMute.class, EvtVoiceMute.class,
                "[voice] member mute")
                .setName("Voice Member Mute")
                .setDesc("Fired when a member gets muted from a voice channel.")
                .setExample("on member mute:");


       EventValues.registerEventValue(EvtVoiceMute.class, Member.class, new Getter<Member, EvtVoiceMute>() {
            @Override
            public Member get(@NotNull EvtVoiceMute event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceMute.class, Guild.class, new Getter<Guild, EvtVoiceMute>() {
            @Override
            public Guild get(@NotNull EvtVoiceMute event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceMute.class, Bot.class, new Getter<Bot, EvtVoiceMute>() {
            @Override
            public Bot get(@NotNull EvtVoiceMute event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtVoiceMute extends SimpleDiSkyEvent<GuildVoiceMuteEvent> implements LogEvent {
        public EvtVoiceMute(VoiceMute event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}