package info.itsthesky.disky3.core.events.voice;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import org.jetbrains.annotations.NotNull;

public class VoiceJoin extends DiSkyEvent<GuildVoiceJoinEvent> {

    static {
        DiSkyEvent.register("Voice Channel Join", VoiceJoin.class, EvtVoiceJoin.class,
                "[(user|member)] voice [channel] join")
                .setName("Voice Channel Join")
                .setDesc("Fired when someone joins in a voice channel.")
                .setExample("on voice channel join:");


        EventValues.registerEventValue(EvtVoiceJoin.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceJoin>() {
            @Override
            public VoiceChannel get(@NotNull EvtVoiceJoin event) {
                return (VoiceChannel) event.getJDAEvent().getNewValue();
            }
        }, 1);

        EventValues.registerEventValue(EvtVoiceJoin.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceJoin>() {
            @Override
            public VoiceChannel get(@NotNull EvtVoiceJoin event) {
                return (VoiceChannel) event.getJDAEvent().getChannelLeft();
            }
        }, -1);

        EventValues.registerEventValue(EvtVoiceJoin.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceJoin>() {
            @Override
            public VoiceChannel get(@NotNull EvtVoiceJoin event) {
                return (VoiceChannel) event.getJDAEvent().getOldValue();
            }
        }, 0);

        EventValues.registerEventValue(EvtVoiceJoin.class, GuildChannel.class, new Getter<GuildChannel, EvtVoiceJoin>() {
            @Override
            public GuildChannel get(@NotNull EvtVoiceJoin event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 0);

        EventValues.registerEventValue(EvtVoiceJoin.class, Member.class, new Getter<Member, EvtVoiceJoin>() {
            @Override
            public Member get(@NotNull EvtVoiceJoin event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EvtVoiceJoin.class, Guild.class, new Getter<Guild, EvtVoiceJoin>() {
            @Override
            public Guild get(@NotNull EvtVoiceJoin event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtVoiceJoin.class, Bot.class, new Getter<Bot, EvtVoiceJoin>() {
            @Override
            public Bot get(@NotNull EvtVoiceJoin event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtVoiceJoin extends SimpleDiSkyEvent<GuildVoiceJoinEvent> {
        public EvtVoiceJoin(VoiceJoin event) { }
    }

}