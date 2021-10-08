package info.itsthesky.disky3.core.events.member;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;

public class MemberBan extends DiSkyEvent<GuildBanEvent> {

    static {
        DiSkyEvent.register("Member Ban", MemberBan.class, EvtMemberBan.class,
                "[discord] [guild] (member|user) ban")
                .setName("Member Ban")
                .setDesc("Fired when any member or bot is banned from a guild. You can only get the event user, the event member is not available since the user is not in the guild anymore!")
                .setExample("on guild member ban");


       EventValues.registerEventValue(EvtMemberBan.class, User.class, new Getter<User, EvtMemberBan>() {
            @Override
            public User get(EvtMemberBan event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtMemberBan.class, Guild.class, new Getter<Guild, EvtMemberBan>() {
            @Override
            public Guild get(EvtMemberBan event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtMemberBan.class, Bot.class, new Getter<Bot, EvtMemberBan>() {
            @Override
            public Bot get(EvtMemberBan event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtMemberBan extends SimpleDiSkyEvent<GuildBanEvent> implements LogEvent {
        public EvtMemberBan(MemberBan event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}