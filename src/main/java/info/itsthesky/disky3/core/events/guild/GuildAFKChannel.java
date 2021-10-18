package info.itsthesky.disky3.core.events.guild;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateAfkChannelEvent;
import org.jetbrains.annotations.NotNull;

public class GuildAFKChannel extends DiSkyEvent<GuildUpdateAfkChannelEvent> {

    static {
        DiSkyEvent.register("AFK Channel Update", GuildAFKChannel.class, EvtGuildAFKChannel.class,
                "[discord] [guild] afk channel (update|change)")
                .setName("AFK Channel Update")
                .setDesc("Fired when the AFK Channel of a guild changes.")
                .setExample("on guild afk channel change:");


       EventValues.registerEventValue(EvtGuildAFKChannel.class, VoiceChannel.class, new Getter<VoiceChannel, EvtGuildAFKChannel>() {
            @Override
            public VoiceChannel get(@NotNull EvtGuildAFKChannel event) {
                return event.getJDAEvent().getOldAfkChannel();
            }
        }, -1);

        EventValues.registerEventValue(EvtGuildAFKChannel.class, VoiceChannel.class, new Getter<VoiceChannel, EvtGuildAFKChannel>() {
            @Override
            public VoiceChannel get(@NotNull EvtGuildAFKChannel event) {
                return event.getJDAEvent().getNewAfkChannel();
            }
        }, 1);

        EventValues.registerEventValue(EvtGuildAFKChannel.class, VoiceChannel.class, new Getter<VoiceChannel, EvtGuildAFKChannel>() {
            @Override
            public VoiceChannel get(@NotNull EvtGuildAFKChannel event) {
                return event.getJDAEvent().getNewAfkChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildAFKChannel.class, Guild.class, new Getter<Guild, EvtGuildAFKChannel>() {
            @Override
            public Guild get(@NotNull EvtGuildAFKChannel event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildAFKChannel.class, Bot.class, new Getter<Bot, EvtGuildAFKChannel>() {
            @Override
            public Bot get(@NotNull EvtGuildAFKChannel event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtGuildAFKChannel extends SimpleDiSkyEvent<GuildUpdateAfkChannelEvent> implements LogEvent {
        public EvtGuildAFKChannel(GuildAFKChannel event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}