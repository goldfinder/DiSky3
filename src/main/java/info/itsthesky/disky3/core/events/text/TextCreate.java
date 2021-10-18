package info.itsthesky.disky3.core.events.text;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class TextCreate extends DiSkyEvent<ChannelCreateEvent> {

    @Override
    protected Predicate<ChannelCreateEvent> checker() {
        return e -> e.isFromType(ChannelType.TEXT);
    }

    static {
        DiSkyEvent.register("Text Channel Create", TextCreate.class, EvtTextCreate.class,
                "[discord] text [channel] create")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtTextCreate.class, Guild.class, new Getter<Guild, EvtTextCreate>() {
            @Override
            public Guild get(@NotNull EvtTextCreate event) {
                return ((TextChannel) event.getJDAEvent().getChannel()).getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextCreate.class, TextChannel.class, new Getter<TextChannel, EvtTextCreate>() {
            @Override
            public TextChannel get(@NotNull EvtTextCreate event) {
                return (TextChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextCreate.class, Bot.class, new Getter<Bot, EvtTextCreate>() {
            @Override
            public Bot get(@NotNull EvtTextCreate event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtTextCreate extends SimpleDiSkyEvent<ChannelCreateEvent> implements LogEvent {
        public EvtTextCreate(TextCreate event) { }

        @Override
        public User getActionAuthor() {
            return ((TextChannel) getJDAEvent().getChannel()).getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}