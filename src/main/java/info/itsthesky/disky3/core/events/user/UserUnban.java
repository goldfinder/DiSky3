package info.itsthesky.disky3.core.events.user;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;

public class UserUnban extends DiSkyEvent<GuildUnbanEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", UserUnban.class, EvtUserUnban.class,
                "guild [(user|member)] unban")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtUserUnban.class, User.class, new Getter<User, EvtUserUnban>() {
            @Override
            public User get(EvtUserUnban event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtUserUnban.class, Guild.class, new Getter<Guild, EvtUserUnban>() {
            @Override
            public Guild get(EvtUserUnban event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtUserUnban.class, Bot.class, new Getter<Bot, EvtUserUnban>() {
            @Override
            public Bot get(EvtUserUnban event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtUserUnban extends SimpleDiSkyEvent<GuildUnbanEvent> implements LogEvent {
        public EvtUserUnban(UserUnban event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}