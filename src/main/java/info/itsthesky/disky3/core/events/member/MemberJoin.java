package info.itsthesky.disky3.core.events.member;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class MemberJoin extends DiSkyEvent<GuildMemberJoinEvent> {

    static {
        DiSkyEvent.register("Member Join", MemberJoin.class, EvtMemberJoin.class,
                "member join [guild]")
                .setName("Member Join")
                .setDesc("Fired when a member join a guild. This is not fired if it's the bot itself that joined a new guild.")
                .setExample("on member join");


        EventValues.registerEventValue(EvtMemberJoin.class, Member.class, new Getter<Member, EvtMemberJoin>() {
            @Override
            public Member get(EvtMemberJoin event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EvtMemberJoin.class, User.class, new Getter<User, EvtMemberJoin>() {
            @Override
            public User get(EvtMemberJoin event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

        EventValues.registerEventValue(EvtMemberJoin.class, Guild.class, new Getter<Guild, EvtMemberJoin>() {
            @Override
            public Guild get(EvtMemberJoin event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtMemberJoin.class, Bot.class, new Getter<Bot, EvtMemberJoin>() {
            @Override
            public Bot get(EvtMemberJoin event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtMemberJoin extends SimpleDiSkyEvent<GuildMemberJoinEvent> {
        public EvtMemberJoin(MemberJoin event) { }
    }

}