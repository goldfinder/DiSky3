package info.itsthesky.disky3.core.events.voice;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class VoiceLeave extends DiSkyEvent<GuildVoiceLeaveEvent> {

    static {
        DiSkyEvent.register("Voice Channel Leave", VoiceLeave.class, EvtVoiceLeave.class,
                "[(user|member)] voice [channel] leave")
                .setName("Voice Channel Leave")
                .setDesc("Fired when someone leaves a voice channel.")
                .setExample("on voice leave:");


        EventValues.registerEventValue(EvtVoiceLeave.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceLeave>() {
            @Override
            public VoiceChannel get(@NotNull EvtVoiceLeave event) {
                return (VoiceChannel) event.getJDAEvent().getOldValue();
            }
        }, -1);

        EventValues.registerEventValue(EvtVoiceLeave.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceLeave>() {
            @Override
            public VoiceChannel get(@NotNull EvtVoiceLeave event) {
                return (VoiceChannel) event.getJDAEvent().getNewValue();
            }
        }, 1);

        EventValues.registerEventValue(EvtVoiceLeave.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceLeave>() {
            @Override
            public VoiceChannel get(@NotNull EvtVoiceLeave event) {
                return (VoiceChannel) event.getJDAEvent().getOldValue();
            }
        }, 0);

        EventValues.registerEventValue(EvtVoiceLeave.class, Member.class, new Getter<Member, EvtVoiceLeave>() {
            @Override
            public Member get(@NotNull EvtVoiceLeave event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EvtVoiceLeave.class, Guild.class, new Getter<Guild, EvtVoiceLeave>() {
            @Override
            public Guild get(@NotNull EvtVoiceLeave event) {
                return event.getJDAEvent().getChannelLeft().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtVoiceLeave.class, Bot.class, new Getter<Bot, EvtVoiceLeave>() {
            @Override
            public Bot get(@NotNull EvtVoiceLeave event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtVoiceLeave extends SimpleDiSkyEvent<GuildVoiceLeaveEvent> {
        public EvtVoiceLeave(VoiceLeave event) { }
    }

}