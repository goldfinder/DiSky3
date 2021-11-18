package info.itsthesky.disky3.core.events.threads;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.ThreadChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.thread.ThreadRevealedEvent;
import net.dv8tion.jda.api.events.thread.member.ThreadMemberJoinEvent;
import org.jetbrains.annotations.NotNull;

public class ThreadReveal extends DiSkyEvent<ThreadRevealedEvent> {

    static {
        DiSkyEvent.register("Thread Reveal", ThreadReveal.class, EvtThreadReveal.class,
                "thread [member] reveal[ed]")
                .setDesc("Fired when someone reveal a thread.",
                        "Of course this thread was been hidden before.",
                        "This is a log event, so event-author will work here.")
                .setExample("on thread reveal:");


       EventValues.registerEventValue(EvtThreadReveal.class, Guild.class, new Getter<Guild, EvtThreadReveal>() {
            @Override
            public Guild get(@NotNull EvtThreadReveal event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtThreadReveal.class, ThreadChannel.class, new Getter<ThreadChannel, EvtThreadReveal>() {
            @Override
            public ThreadChannel get(@NotNull EvtThreadReveal event) {
                return event.getJDAEvent().getThread();
            }
        }, 0);

       EventValues.registerEventValue(EvtThreadReveal.class, Bot.class, new Getter<Bot, EvtThreadReveal>() {
            @Override
            public Bot get(@NotNull EvtThreadReveal event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtThreadReveal extends SimpleDiSkyEvent<ThreadRevealedEvent> implements LogEvent {
        public EvtThreadReveal(ThreadReveal event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}