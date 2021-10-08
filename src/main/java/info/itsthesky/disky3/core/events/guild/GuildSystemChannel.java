package info.itsthesky.disky3.core.events.guild;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateSystemChannelEvent;

public class GuildSystemChannel extends DiSkyEvent<GuildUpdateSystemChannelEvent> {

    static {
        DiSkyEvent.register("Guild System Channel Update", GuildSystemChannel.class, EvtGuildSystemChannel.class,
                "[guild] system channel (change|update)")
                .setName("Guild System Channel Update");


        EventValues.registerEventValue(EvtGuildSystemChannel.class, TextChannel.class, new Getter<TextChannel, EvtGuildSystemChannel>() {
            @Override
            public TextChannel get(EvtGuildSystemChannel event) {
                return event.getJDAEvent().getNewSystemChannel();
            }
        }, 1);

        EventValues.registerEventValue(EvtGuildSystemChannel.class, TextChannel.class, new Getter<TextChannel, EvtGuildSystemChannel>() {
            @Override
            public TextChannel get(EvtGuildSystemChannel event) {
                return event.getJDAEvent().getNewSystemChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildSystemChannel.class, TextChannel.class, new Getter<TextChannel, EvtGuildSystemChannel>() {
            @Override
            public TextChannel get(EvtGuildSystemChannel event) {
                return event.getJDAEvent().getOldSystemChannel();
            }
        }, -1);

       EventValues.registerEventValue(EvtGuildSystemChannel.class, Guild.class, new Getter<Guild, EvtGuildSystemChannel>() {
            @Override
            public Guild get(EvtGuildSystemChannel event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildSystemChannel.class, Guild.class, new Getter<Guild, EvtGuildSystemChannel>() {
            @Override
            public Guild get(EvtGuildSystemChannel event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildSystemChannel.class, Bot.class, new Getter<Bot, EvtGuildSystemChannel>() {
            @Override
            public Bot get(EvtGuildSystemChannel event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtGuildSystemChannel extends SimpleDiSkyEvent<GuildUpdateSystemChannelEvent> implements LogEvent {
        public EvtGuildSystemChannel(GuildSystemChannel event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}