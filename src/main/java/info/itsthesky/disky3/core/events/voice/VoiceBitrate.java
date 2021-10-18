package info.itsthesky.disky3.core.events.voice;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.update.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class VoiceBitrate extends DiSkyEvent<ChannelUpdateBitrateEvent> {

    @Override
    protected Predicate<ChannelUpdateBitrateEvent> checker() {
        return e -> e.isFromType(ChannelType.VOICE);
    }

    static {
        DiSkyEvent.register("Voice Bitrate Change", VoiceBitrate.class, EvtVoiceBitrate.class,
                "[discord] voice [channel] bitrate (update|change)")
                .setName("Voice Bitrate Change")
                .setDesc("Fired when the bitrate of a voice channel changes.")
                .setExample("on voice bitrate:");


       EventValues.registerEventValue(EvtVoiceBitrate.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceBitrate>() {
            @Override
            public VoiceChannel get(@NotNull EvtVoiceBitrate event) {
                return ((VoiceChannel) event.getJDAEvent().getChannel());
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceBitrate.class, Guild.class, new Getter<Guild, EvtVoiceBitrate>() {
            @Override
            public Guild get(@NotNull EvtVoiceBitrate event) {
                return ((VoiceChannel) event.getJDAEvent().getChannel()).getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceBitrate.class, Bot.class, new Getter<Bot, EvtVoiceBitrate>() {
            @Override
            public Bot get(@NotNull EvtVoiceBitrate event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtVoiceBitrate extends SimpleDiSkyEvent<ChannelUpdateBitrateEvent> {
        public EvtVoiceBitrate(VoiceBitrate event) { }
    }

}