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

public class VoicePosition extends DiSkyEvent<ChannelUpdatePositionEvent> {


    @Override
    protected Predicate<ChannelUpdatePositionEvent> checker() {
        return e -> e.isFromType(ChannelType.VOICE);
    }

    static {
        DiSkyEvent.register("Inner Event Name", VoicePosition.class, EvtVoicePosition.class,
                "[discord] voice [channel] position (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtVoicePosition.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoicePosition>() {
            @Override
            public VoiceChannel get(EvtVoicePosition event) {
                return ((VoiceChannel) event.getJDAEvent().getChannel());
            }
        }, 0);

       EventValues.registerEventValue(EvtVoicePosition.class, Guild.class, new Getter<Guild, EvtVoicePosition>() {
            @Override
            public Guild get(EvtVoicePosition event) {
                return ((VoiceChannel) event.getJDAEvent().getChannel()).getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoicePosition.class, Bot.class, new Getter<Bot, EvtVoicePosition>() {
            @Override
            public Bot get(EvtVoicePosition event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtVoicePosition extends SimpleDiSkyEvent<ChannelUpdatePositionEvent> {
        public EvtVoicePosition(VoicePosition event) { }
    }

}