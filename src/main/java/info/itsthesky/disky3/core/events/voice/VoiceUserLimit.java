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

public class VoiceUserLimit extends DiSkyEvent<ChannelUpdateUserLimitEvent> {

    @Override
    protected Predicate<ChannelUpdateUserLimitEvent> checker() {
        return e -> e.isFromType(ChannelType.VOICE);
    }

    static {
        DiSkyEvent.register("Voice Max User Change", VoiceUserLimit.class, EvtVoiceUserLimit.class,
                "[discord] voice [channel] (max user|user limit) (update|change)")
                .setName("Voice User Limit Change")
                .setDesc("Fired when the max user limit of a voice channel changes.")
                .setExample("on voice max user change:");


       EventValues.registerEventValue(EvtVoiceUserLimit.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceUserLimit>() {
            @Override
            public VoiceChannel get(@NotNull EvtVoiceUserLimit event) {
                return ((VoiceChannel) event.getJDAEvent().getChannel());
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceUserLimit.class, Guild.class, new Getter<Guild, EvtVoiceUserLimit>() {
            @Override
            public Guild get(@NotNull EvtVoiceUserLimit event) {
                return ((VoiceChannel) event.getJDAEvent().getChannel()).getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceUserLimit.class, Bot.class, new Getter<Bot, EvtVoiceUserLimit>() {
            @Override
            public Bot get(@NotNull EvtVoiceUserLimit event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtVoiceUserLimit extends SimpleDiSkyEvent<ChannelUpdateUserLimitEvent> {
        public EvtVoiceUserLimit(VoiceUserLimit event) { }
    }

}