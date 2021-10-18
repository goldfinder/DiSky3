package info.itsthesky.disky3.core.events.member;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import org.jetbrains.annotations.NotNull;

public class MemberNickChange extends DiSkyEvent<GuildMemberUpdateNicknameEvent> {

    static {
        DiSkyEvent.register("Nickname Change", MemberNickChange.class, EvtMemberNickChange.class,
                "[discord] [guild] member nick[name] (change|update)")
                .setName("Nickname Change")
                .setDesc("Fired when someone changes its nickname to another one or reset it.")
                .setExample("on member nick update:");


       EventValues.registerEventValue(EvtMemberNickChange.class, Member.class, new Getter<Member, EvtMemberNickChange>() {
            @Override
            public Member get(@NotNull EvtMemberNickChange event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

       EventValues.registerEventValue(EvtMemberNickChange.class, User.class, new Getter<User, EvtMemberNickChange>() {
            @Override
            public User get(@NotNull EvtMemberNickChange event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtMemberNickChange.class, Guild.class, new Getter<Guild, EvtMemberNickChange>() {
            @Override
            public Guild get(@NotNull EvtMemberNickChange event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtMemberNickChange.class, Bot.class, new Getter<Bot, EvtMemberNickChange>() {
            @Override
            public Bot get(@NotNull EvtMemberNickChange event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

        EventValues.registerEventValue(EvtMemberNickChange.class, String.class, new Getter<String, EvtMemberNickChange>() {
            @Override
            public String get(@NotNull EvtMemberNickChange event) {
                return event.getJDAEvent().getNewNickname();
            }
        }, 1);

        EventValues.registerEventValue(EvtMemberNickChange.class, String.class, new Getter<String, EvtMemberNickChange>() {
            @Override
            public String get(@NotNull EvtMemberNickChange event) {
                return event.getJDAEvent().getNewNickname();
            }
        }, 0);

        EventValues.registerEventValue(EvtMemberNickChange.class, String.class, new Getter<String, EvtMemberNickChange>() {
            @Override
            public String get(@NotNull EvtMemberNickChange event) {
                return event.getJDAEvent().getOldNickname();
            }
        }, -1);

    }

    public static class EvtMemberNickChange extends SimpleDiSkyEvent<GuildMemberUpdateNicknameEvent> implements LogEvent {
        public EvtMemberNickChange(MemberNickChange event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}