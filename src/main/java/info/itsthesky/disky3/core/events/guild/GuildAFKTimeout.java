package info.itsthesky.disky3.core.events.guild;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateAfkTimeoutEvent;
import org.jetbrains.annotations.NotNull;

public class GuildAFKTimeout extends DiSkyEvent<GuildUpdateAfkTimeoutEvent> {

    static {
        DiSkyEvent.register("Guild AFK Timeout Change", GuildAFKTimeout.class, EvtGuildAFKTimeout.class,
                "[discord] [guild] afk timeout (update|change)")
                .setName("Guild AFK Timeout Change")
                .setDesc("Fired when the AFK Timeout time of a guild changes.")
                .setExample("on guild afk timeout update:");


       EventValues.registerEventValue(EvtGuildAFKTimeout.class, Number.class, new Getter<Number, EvtGuildAFKTimeout>() {
            @Override
            public Number get(@NotNull EvtGuildAFKTimeout event) {
                return event.getJDAEvent().getNewValue().getSeconds();
            }
        }, 1);

       EventValues.registerEventValue(EvtGuildAFKTimeout.class, Number.class, new Getter<Number, EvtGuildAFKTimeout>() {
            @Override
            public Number get(@NotNull EvtGuildAFKTimeout event) {
                return event.getJDAEvent().getOldValue().getSeconds();
            }
        }, -1);

       EventValues.registerEventValue(EvtGuildAFKTimeout.class, Guild.class, new Getter<Guild, EvtGuildAFKTimeout>() {
            @Override
            public Guild get(@NotNull EvtGuildAFKTimeout event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildAFKTimeout.class, Guild.class, new Getter<Guild, EvtGuildAFKTimeout>() {
            @Override
            public Guild get(@NotNull EvtGuildAFKTimeout event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildAFKTimeout.class, Bot.class, new Getter<Bot, EvtGuildAFKTimeout>() {
            @Override
            public Bot get(@NotNull EvtGuildAFKTimeout event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtGuildAFKTimeout extends SimpleDiSkyEvent<GuildUpdateAfkTimeoutEvent> implements LogEvent {
        public EvtGuildAFKTimeout(GuildAFKTimeout event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}