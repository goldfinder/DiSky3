package info.itsthesky.disky3.core.events.voice;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateUserLimitEvent;

public class VoiceUserLimit extends DiSkyEvent<VoiceChannelUpdateUserLimitEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", VoiceUserLimit.class, EvtVoiceUserLimit.class,
                "[discord] voice [channel] (max user|user limit) (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtVoiceUserLimit.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceUserLimit>() {
            @Override
            public VoiceChannel get(EvtVoiceUserLimit event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceUserLimit.class, Guild.class, new Getter<Guild, EvtVoiceUserLimit>() {
            @Override
            public Guild get(EvtVoiceUserLimit event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceUserLimit.class, Bot.class, new Getter<Bot, EvtVoiceUserLimit>() {
            @Override
            public Bot get(EvtVoiceUserLimit event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtVoiceUserLimit extends SimpleDiSkyEvent<VoiceChannelUpdateUserLimitEvent> {
        public EvtVoiceUserLimit(VoiceUserLimit event) { }
    }

}