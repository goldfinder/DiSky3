package info.itsthesky.disky3.core.events.voice;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;

public class VoiceJoin extends DiSkyEvent<GuildVoiceJoinEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", VoiceJoin.class, EvtVoiceJoin.class,
                "[(user|member)] voice [channel] join")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


        EventValues.registerEventValue(EvtVoiceJoin.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceJoin>() {
            @Override
            public VoiceChannel get(EvtVoiceJoin event) {
                return (VoiceChannel) event.getJDAEvent().getNewValue();
            }
        }, 1);

        EventValues.registerEventValue(EvtVoiceJoin.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceJoin>() {
            @Override
            public VoiceChannel get(EvtVoiceJoin event) {
                return (VoiceChannel) event.getJDAEvent().getChannelLeft();
            }
        }, -1);

        EventValues.registerEventValue(EvtVoiceJoin.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceJoin>() {
            @Override
            public VoiceChannel get(EvtVoiceJoin event) {
                return (VoiceChannel) event.getJDAEvent().getChannelLeft();
            }
        }, 0);

        EventValues.registerEventValue(EvtVoiceJoin.class, GuildChannel.class, new Getter<GuildChannel, EvtVoiceJoin>() {
            @Override
            public GuildChannel get(EvtVoiceJoin event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 0);

        EventValues.registerEventValue(EvtVoiceJoin.class, Member.class, new Getter<Member, EvtVoiceJoin>() {
            @Override
            public Member get(EvtVoiceJoin event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EvtVoiceJoin.class, Guild.class, new Getter<Guild, EvtVoiceJoin>() {
            @Override
            public Guild get(EvtVoiceJoin event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtVoiceJoin.class, Bot.class, new Getter<Bot, EvtVoiceJoin>() {
            @Override
            public Bot get(EvtVoiceJoin event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtVoiceJoin extends SimpleDiSkyEvent<GuildVoiceJoinEvent> {
        public EvtVoiceJoin(VoiceJoin event) { }
    }

}