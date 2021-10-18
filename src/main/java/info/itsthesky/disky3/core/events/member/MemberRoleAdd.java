package info.itsthesky.disky3.core.events.member;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import org.jetbrains.annotations.NotNull;

public class MemberRoleAdd extends DiSkyEvent<GuildMemberRoleAddEvent> {

    static {
        DiSkyEvent.register("Role Add", MemberRoleAdd.class, EvtMemberRoleAdd.class,
                "[discord] [guild] member role add[ed]")
                .setName("Role Add")
                .setDesc("Fired when someone add a role to another member. It's a log event, so use event-author to see who made the action.")
                .setExample("on member role add:");


       EventValues.registerEventValue(EvtMemberRoleAdd.class, Role[].class, new Getter<Role[], EvtMemberRoleAdd>() {
            @Override
            public Role[] get(@NotNull EvtMemberRoleAdd event) {
                return event.getJDAEvent().getRoles().toArray(new Role[0]);
            }
        }, 0);

       EventValues.registerEventValue(EvtMemberRoleAdd.class, Member.class, new Getter<Member, EvtMemberRoleAdd>() {
            @Override
            public Member get(@NotNull EvtMemberRoleAdd event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

       EventValues.registerEventValue(EvtMemberRoleAdd.class, User.class, new Getter<User, EvtMemberRoleAdd>() {
            @Override
            public User get(@NotNull EvtMemberRoleAdd event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtMemberRoleAdd.class, Guild.class, new Getter<Guild, EvtMemberRoleAdd>() {
            @Override
            public Guild get(@NotNull EvtMemberRoleAdd event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtMemberRoleAdd.class, Bot.class, new Getter<Bot, EvtMemberRoleAdd>() {
            @Override
            public Bot get(@NotNull EvtMemberRoleAdd event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtMemberRoleAdd extends SimpleDiSkyEvent<GuildMemberRoleAddEvent> implements LogEvent {
        public EvtMemberRoleAdd(MemberRoleAdd event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}