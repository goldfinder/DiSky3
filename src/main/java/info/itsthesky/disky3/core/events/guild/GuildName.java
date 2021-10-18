package info.itsthesky.disky3.core.events.guild;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import org.jetbrains.annotations.NotNull;

public class GuildName extends DiSkyEvent<GuildUpdateNameEvent> {

    static {
        DiSkyEvent.register("Guild Name Change", GuildName.class, EvtGuildName.class,
                "[discord] guild name (change|update)")
                .setName("Guild Name Change")
                .setDesc("Fired when the name of a guild changes.")
                .setExample("on guild name change:");

        EventValues.registerEventValue(EvtGuildName.class, String.class, new Getter<String, EvtGuildName>() {
            @Override
            public String get(@NotNull EvtGuildName event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 1);

        EventValues.registerEventValue(EvtGuildName.class, String.class, new Getter<String, EvtGuildName>() {
            @Override
            public String get(@NotNull EvtGuildName event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 0);

        EventValues.registerEventValue(EvtGuildName.class, String.class, new Getter<String, EvtGuildName>() {
            @Override
            public String get(@NotNull EvtGuildName event) {
                return event.getJDAEvent().getOldName();
            }
        }, -1);

        EventValues.registerEventValue(EvtGuildName.class, String.class, new Getter<String, EvtGuildName>() {
            @Override
            public String get(@NotNull EvtGuildName event) {
                return event.getJDAEvent().getOldName();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildName.class, Guild.class, new Getter<Guild, EvtGuildName>() {
            @Override
            public Guild get(@NotNull EvtGuildName event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildName.class, Guild.class, new Getter<Guild, EvtGuildName>() {
            @Override
            public Guild get(@NotNull EvtGuildName event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildName.class, Bot.class, new Getter<Bot, EvtGuildName>() {
            @Override
            public Bot get(@NotNull EvtGuildName event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtGuildName extends SimpleDiSkyEvent<GuildUpdateNameEvent> implements LogEvent {
        public EvtGuildName(GuildName event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}