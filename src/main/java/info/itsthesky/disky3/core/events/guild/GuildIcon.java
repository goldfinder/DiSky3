package info.itsthesky.disky3.core.events.guild;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateIconEvent;

public class GuildIcon extends DiSkyEvent<GuildUpdateIconEvent> {

    static {
        DiSkyEvent.register("Guild Icon Update", GuildIcon.class, EvtGuildIcon.class,
                "[guild] icon (update|change)]")
                .setName("Guild Icon Update");


        EventValues.registerEventValue(EvtGuildIcon.class, Guild.class, new Getter<Guild, EvtGuildIcon>() {
            @Override
            public Guild get(EvtGuildIcon event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

        EventValues.registerEventValue(EvtGuildIcon.class, String.class, new Getter<String, EvtGuildIcon>() {
            @Override
            public String get(EvtGuildIcon event) {
                return event.getJDAEvent().getNewIconUrl();
            }
        }, 1);

        EventValues.registerEventValue(EvtGuildIcon.class, String.class, new Getter<String, EvtGuildIcon>() {
            @Override
            public String get(EvtGuildIcon event) {
                return event.getJDAEvent().getNewIconUrl();
            }
        }, 0);

        EventValues.registerEventValue(EvtGuildIcon.class, String.class, new Getter<String, EvtGuildIcon>() {
            @Override
            public String get(EvtGuildIcon event) {
                return event.getJDAEvent().getOldIconUrl();
            }
        }, -1);

       EventValues.registerEventValue(EvtGuildIcon.class, Bot.class, new Getter<Bot, EvtGuildIcon>() {
            @Override
            public Bot get(EvtGuildIcon event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtGuildIcon extends SimpleDiSkyEvent<GuildUpdateIconEvent> implements LogEvent {
        public EvtGuildIcon(GuildIcon event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}