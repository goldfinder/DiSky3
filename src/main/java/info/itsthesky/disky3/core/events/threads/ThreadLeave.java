package info.itsthesky.disky3.core.events.threads;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildThread;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.thread.member.ThreadMemberJoinEvent;
import net.dv8tion.jda.api.events.thread.member.ThreadMemberLeaveEvent;
import org.jetbrains.annotations.NotNull;

public class ThreadLeave extends DiSkyEvent<ThreadMemberLeaveEvent> {

    static {
        DiSkyEvent.register("Thread Leave", ThreadLeave.class, EvtThreadLeave.class,
                "thread [member] (leave|quit)")
                .setName("Thread Leave")
                .setDesc("Fired when a someone leaves a thread.")
                .setExample("on thread leave:");


       EventValues.registerEventValue(EvtThreadLeave.class, Guild.class, new Getter<Guild, EvtThreadLeave>() {
            @Override
            public Guild get(@NotNull EvtThreadLeave event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtThreadLeave.class, GuildThread.class, new Getter<GuildThread, EvtThreadLeave>() {
            @Override
            public GuildThread get(@NotNull EvtThreadLeave event) {
                return event.getJDAEvent().getThread();
            }
        }, 0);

        EventValues.registerEventValue(EvtThreadLeave.class, Member.class, new Getter<Member, EvtThreadLeave>() {
            @Override
            public Member get(@NotNull EvtThreadLeave event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

       EventValues.registerEventValue(EvtThreadLeave.class, Bot.class, new Getter<Bot, EvtThreadLeave>() {
            @Override
            public Bot get(@NotNull EvtThreadLeave event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtThreadLeave extends SimpleDiSkyEvent<ThreadMemberJoinEvent> {
        public EvtThreadLeave(ThreadLeave event) { }
    }

}