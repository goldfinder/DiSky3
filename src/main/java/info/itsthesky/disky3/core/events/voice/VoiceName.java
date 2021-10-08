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

import java.util.function.Predicate;

public class VoiceName extends DiSkyEvent<ChannelUpdateNameEvent> {

    @Override
    protected Predicate<ChannelUpdateNameEvent> checker() {
        return e -> e.isFromType(ChannelType.VOICE);
    }

    static {
        DiSkyEvent.register("Inner Event Name", VoiceName.class, EvtVoiceName.class,
                "[discord] voice [channel] name (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


        EventValues.registerEventValue(EvtVoiceName.class, String.class, new Getter<String, EvtVoiceName>() {
            @Override
            public String get(EvtVoiceName event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 1);

        EventValues.registerEventValue(EvtVoiceName.class, String.class, new Getter<String, EvtVoiceName>() {
            @Override
            public String get(EvtVoiceName event) {
                return event.getJDAEvent().getOldValue();
            }
        }, -1);

        EventValues.registerEventValue(EvtVoiceName.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceName>() {
            @Override
            public VoiceChannel get(EvtVoiceName event) {
                return ((VoiceChannel) event.getJDAEvent().getChannel());
            }
        }, 0);

        EventValues.registerEventValue(EvtVoiceName.class, Guild.class, new Getter<Guild, EvtVoiceName>() {
            @Override
            public Guild get(EvtVoiceName event) {
                return ((VoiceChannel) event.getJDAEvent().getChannel()).getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtVoiceName.class, Bot.class, new Getter<Bot, EvtVoiceName>() {
            @Override
            public Bot get(EvtVoiceName event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtVoiceName extends SimpleDiSkyEvent<ChannelUpdateNameEvent> {
        public EvtVoiceName(VoiceName event) { }
    }

}