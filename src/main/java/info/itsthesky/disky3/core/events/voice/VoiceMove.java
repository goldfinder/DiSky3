package info.itsthesky.disky3.core.events.voice;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import org.jetbrains.annotations.NotNull;

public class VoiceMove extends DiSkyEvent<GuildVoiceMoveEvent> {

    static {
        DiSkyEvent.register("Voice Member Move", VoiceMove.class, EvtVoiceMove.class,
                "[voice] member move")
                .setName("Voice Channel Move")
                .setDesc("Fired when a member moves to another voice channel.")
                .setExample("on member move:");


        EventValues.registerEventValue(EvtVoiceMove.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceMove>() {
            @Override
            public VoiceChannel get(@NotNull EvtVoiceMove event) {
                return (VoiceChannel) event.getJDAEvent().getNewValue();
            }
        }, 1);

        EventValues.registerEventValue(EvtVoiceMove.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceMove>() {
            @Override
            public VoiceChannel get(@NotNull EvtVoiceMove event) {
                return (VoiceChannel) event.getJDAEvent().getNewValue();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceMove.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceMove>() {
            @Override
            public VoiceChannel get(@NotNull EvtVoiceMove event) {
                return (VoiceChannel) event.getJDAEvent().getChannelLeft();
            }
        }, -1);

       EventValues.registerEventValue(EvtVoiceMove.class, Member.class, new Getter<Member, EvtVoiceMove>() {
            @Override
            public Member get(@NotNull EvtVoiceMove event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceMove.class, Guild.class, new Getter<Guild, EvtVoiceMove>() {
            @Override
            public Guild get(@NotNull EvtVoiceMove event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceMove.class, Bot.class, new Getter<Bot, EvtVoiceMove>() {
            @Override
            public Bot get(@NotNull EvtVoiceMove event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtVoiceMove extends SimpleDiSkyEvent<GuildVoiceMoveEvent> implements LogEvent {
        public EvtVoiceMove(VoiceMove event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}