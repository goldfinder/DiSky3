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

public class VoiceRegion extends DiSkyEvent<ChannelUpdateRegionEvent> {

    @Override
    protected Predicate<ChannelUpdateRegionEvent> checker() {
        return e -> e.isFromType(ChannelType.VOICE);
    }

    static {
        DiSkyEvent.register("Voice Region Update", VoiceRegion.class, EvtVoiceRegion.class,
                "[discord] voice [channel] region (update|change)")
                .setName("Voice Region Update")
                .setDesc("Fired when the region of a voice channel changes.")
                .setExample("on voice region change:");


       EventValues.registerEventValue(EvtVoiceRegion.class, String.class, new Getter<String, EvtVoiceRegion>() {
            @Override
            public String get(@NotNull EvtVoiceRegion event) {
                return event.getJDAEvent().getOldValue().getName();
            }
        }, -1);

       EventValues.registerEventValue(EvtVoiceRegion.class, String.class, new Getter<String, EvtVoiceRegion>() {
            @Override
            public String get(@NotNull EvtVoiceRegion event) {
                return event.getJDAEvent().getNewValue().getName();
            }
        }, 1);

       EventValues.registerEventValue(EvtVoiceRegion.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceRegion>() {
            @Override
            public VoiceChannel get(@NotNull EvtVoiceRegion event) {
                return ((VoiceChannel) event.getJDAEvent().getChannel());
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceRegion.class, Guild.class, new Getter<Guild, EvtVoiceRegion>() {
            @Override
            public Guild get(@NotNull EvtVoiceRegion event) {
                return ((VoiceChannel) event.getJDAEvent().getChannel()).getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceRegion.class, Bot.class, new Getter<Bot, EvtVoiceRegion>() {
            @Override
            public Bot get(@NotNull EvtVoiceRegion event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtVoiceRegion extends SimpleDiSkyEvent<ChannelUpdateRegionEvent> {
        public EvtVoiceRegion(VoiceRegion event) { }
    }

}