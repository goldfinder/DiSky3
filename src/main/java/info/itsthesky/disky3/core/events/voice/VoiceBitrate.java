package info.itsthesky.disky3.core.events.voice;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateBitrateEvent;

public class VoiceBitrate extends DiSkyEvent<VoiceChannelUpdateBitrateEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", VoiceBitrate.class, EvtVoiceBitrate.class,
                "[discord] voice [channel] bitrate (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtVoiceBitrate.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceBitrate>() {
            @Override
            public VoiceChannel get(EvtVoiceBitrate event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceBitrate.class, Guild.class, new Getter<Guild, EvtVoiceBitrate>() {
            @Override
            public Guild get(EvtVoiceBitrate event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceBitrate.class, Bot.class, new Getter<Bot, EvtVoiceBitrate>() {
            @Override
            public Bot get(EvtVoiceBitrate event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtVoiceBitrate extends SimpleDiSkyEvent<VoiceChannelUpdateBitrateEvent> {
        public EvtVoiceBitrate(VoiceBitrate event) { }
    }

}