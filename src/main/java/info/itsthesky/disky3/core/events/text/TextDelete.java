package info.itsthesky.disky3.core.events.text;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class TextDelete extends DiSkyEvent<ChannelDeleteEvent> {

    @Override
    protected Predicate<ChannelDeleteEvent> checker() {
        return e -> e.isFromType(ChannelType.TEXT);
    }

    static {
        DiSkyEvent.register("Text Channel Delete", TextDelete.class, EvtTextDelete.class,
                "text [channel] delete")
                .setName("Text Channel Delete")
                .setDesc("Fired when a text channel is deleted")
                .setExample("on text channel delete:");

        EventValues.registerEventValue(EvtTextDelete.class, TextChannel.class, new Getter<TextChannel, EvtTextDelete>() {
            @Override
            public TextChannel get(@NotNull EvtTextDelete event) {
                return (TextChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtTextDelete.class, Guild.class, new Getter<Guild, EvtTextDelete>() {
            @Override
            public Guild get(@NotNull EvtTextDelete event) {
                return ((TextChannel) event.getJDAEvent().getChannel()).getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtTextDelete.class, Bot.class, new Getter<Bot, EvtTextDelete>() {
            @Override
            public Bot get(@NotNull EvtTextDelete event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);
    }

    public static class EvtTextDelete extends SimpleDiSkyEvent<ChannelDeleteEvent> implements LogEvent {
        public EvtTextDelete(TextDelete event) { }

        @Override
        public User getActionAuthor() {
            return ((TextChannel) getJDAEvent().getChannel()).getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}