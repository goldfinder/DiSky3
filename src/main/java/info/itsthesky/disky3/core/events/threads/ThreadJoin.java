package info.itsthesky.disky3.core.events.threads;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.thread.member.ThreadMemberJoinEvent;
import org.jetbrains.annotations.NotNull;

public class ThreadJoin extends DiSkyEvent<ThreadMemberJoinEvent> {

    static {
        DiSkyEvent.register("Thread Join", ThreadJoin.class, EvtThreadJoin.class,
                "thread [member] join")
                .setName("Thread Join")
                .setDesc("Fired when someone joins a thread.")
                .setExample("on thread join:");


       EventValues.registerEventValue(EvtThreadJoin.class, Guild.class, new Getter<Guild, EvtThreadJoin>() {
            @Override
            public Guild get(@NotNull EvtThreadJoin event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtThreadJoin.class, ThreadChannel.class, new Getter<ThreadChannel, EvtThreadJoin>() {
            @Override
            public ThreadChannel get(@NotNull EvtThreadJoin event) {
                return event.getJDAEvent().getThread();
            }
        }, 0);

        EventValues.registerEventValue(EvtThreadJoin.class, Member.class, new Getter<Member, EvtThreadJoin>() {
            @Override
            public Member get(@NotNull EvtThreadJoin event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

       EventValues.registerEventValue(EvtThreadJoin.class, Bot.class, new Getter<Bot, EvtThreadJoin>() {
            @Override
            public Bot get(@NotNull EvtThreadJoin event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtThreadJoin extends SimpleDiSkyEvent<ThreadMemberJoinEvent> {
        public EvtThreadJoin(ThreadJoin event) { }
    }

}