package info.itsthesky.disky3.core.events.threads;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ThreadChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.thread.ThreadHiddenEvent;
import net.dv8tion.jda.api.events.thread.ThreadRevealedEvent;
import org.jetbrains.annotations.NotNull;

public class ThreadHide extends DiSkyEvent<ThreadHiddenEvent> {

    static {
        DiSkyEvent.register("Thread Hide", ThreadHide.class, EvtThreadHide.class,
                "thread [member] (hide|hidden)")
                .setDesc("Fired when someone hide a thread.",
                        "Of course this thread was been revealed (everyone could see it) before.",
                        "This is a log event, so event-author will work here.")
                .setExample("on thread hide:");


       EventValues.registerEventValue(EvtThreadHide.class, Guild.class, new Getter<Guild, EvtThreadHide>() {
            @Override
            public Guild get(@NotNull EvtThreadHide event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtThreadHide.class, ThreadChannel.class, new Getter<ThreadChannel, EvtThreadHide>() {
            @Override
            public ThreadChannel get(@NotNull EvtThreadHide event) {
                return event.getJDAEvent().getThread();
            }
        }, 0);

       EventValues.registerEventValue(EvtThreadHide.class, Bot.class, new Getter<Bot, EvtThreadHide>() {
            @Override
            public Bot get(@NotNull EvtThreadHide event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtThreadHide extends SimpleDiSkyEvent<ThreadHiddenEvent> implements LogEvent {
        public EvtThreadHide(ThreadHide event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}