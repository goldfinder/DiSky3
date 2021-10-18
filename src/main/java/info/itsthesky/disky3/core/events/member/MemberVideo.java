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
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceVideoEvent;
import org.jetbrains.annotations.NotNull;

public class MemberVideo extends DiSkyEvent<GuildVoiceVideoEvent> {

    static {
        DiSkyEvent.register("Member Video", MemberVideo.class, EvtMemberVideo.class,
                "[discord] [guild] (member|user) send[ing] [a] video")
                .setName("Member Ban")
                .setDesc("Fired when any member send a video (enabled camera).")
                .setExample("on guild member send video");


        EventValues.registerEventValue(EvtMemberVideo.class, User.class, new Getter<User, EvtMemberVideo>() {
            @Override
            public User get(@NotNull MemberVideo.EvtMemberVideo event) {
                return event.getJDAEvent().getMember().getUser();
            }
        }, 0);

        EventValues.registerEventValue(EvtMemberVideo.class, Member.class, new Getter<Member, EvtMemberVideo>() {
            @Override
            public Member get(@NotNull MemberVideo.EvtMemberVideo event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EvtMemberVideo.class, VoiceChannel.class, new Getter<VoiceChannel, EvtMemberVideo>() {
            @Override
            public VoiceChannel get(@NotNull MemberVideo.EvtMemberVideo event) {
                return (VoiceChannel) event.getJDAEvent().getVoiceState().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtMemberVideo.class, Guild.class, new Getter<Guild, EvtMemberVideo>() {
            @Override
            public Guild get(@NotNull MemberVideo.EvtMemberVideo event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtMemberVideo.class, Bot.class, new Getter<Bot, EvtMemberVideo>() {
            @Override
            public Bot get(@NotNull MemberVideo.EvtMemberVideo event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtMemberVideo extends SimpleDiSkyEvent<GuildVoiceVideoEvent> implements LogEvent {
        public EvtMemberVideo(MemberVideo event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}