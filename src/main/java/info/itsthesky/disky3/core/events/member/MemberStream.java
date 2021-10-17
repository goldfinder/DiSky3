package info.itsthesky.disky3.core.events.member;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceStreamEvent;
import org.jetbrains.annotations.NotNull;

public class MemberStream extends DiSkyEvent<GuildVoiceStreamEvent> {

    static {
        DiSkyEvent.register("Member Video", MemberStream.class, EvtMemberStream.class,
                "[discord] [guild] (member|user) stream")
                .setName("Member Stream")
                .setDesc("Fired when any member start streaming in a specific voice channel.")
                .setExample("on member stream");


        EventValues.registerEventValue(EvtMemberStream.class, User.class, new Getter<User, EvtMemberStream>() {
            @Override
            public User get(@NotNull MemberStream.EvtMemberStream event) {
                return event.getJDAEvent().getMember().getUser();
            }
        }, 0);

        EventValues.registerEventValue(EvtMemberStream.class, Member.class, new Getter<Member, EvtMemberStream>() {
            @Override
            public Member get(@NotNull MemberStream.EvtMemberStream event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EvtMemberStream.class, VoiceChannel.class, new Getter<VoiceChannel, EvtMemberStream>() {
            @Override
            public VoiceChannel get(@NotNull MemberStream.EvtMemberStream event) {
                return (VoiceChannel) event.getJDAEvent().getVoiceState().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtMemberStream.class, Guild.class, new Getter<Guild, EvtMemberStream>() {
            @Override
            public Guild get(@NotNull MemberStream.EvtMemberStream event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtMemberStream.class, Bot.class, new Getter<Bot, EvtMemberStream>() {
            @Override
            public Bot get(@NotNull MemberStream.EvtMemberStream event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtMemberStream extends SimpleDiSkyEvent<GuildVoiceStreamEvent> implements LogEvent {
        public EvtMemberStream(MemberStream event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}