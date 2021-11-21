package info.itsthesky.disky3.core.events.text;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.update.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class TextTopic extends DiSkyEvent<ChannelUpdateTopicEvent> {

    @Override
    protected Predicate<ChannelUpdateTopicEvent> checker() {
        return e -> e.isFromType(ChannelType.TEXT);
    }

    static {
        DiSkyEvent.register("Text Topic Change", TextTopic.class, EvtTextTopic.class,
                "[discord] text [channel] topic (update|change)")
                .setName("Text Channel Topic Change")
                .setDesc("Fired when the topic of a text channel changes.")
                .setExample("on text channel topic change:");


       EventValues.registerEventValue(EvtTextTopic.class, String.class, new Getter<String, EvtTextTopic>() {
            @Override
            public String get(@NotNull EvtTextTopic event) {
                return event.getJDAEvent().getOldValue();
            }
        }, -1);

       EventValues.registerEventValue(EvtTextTopic.class, String.class, new Getter<String, EvtTextTopic>() {
            @Override
            public String get(@NotNull EvtTextTopic event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 1);

       EventValues.registerEventValue(EvtTextTopic.class, TextChannel.class, new Getter<TextChannel, EvtTextTopic>() {
            @Override
            public TextChannel get(@NotNull EvtTextTopic event) {
                return (TextChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextTopic.class, Guild.class, new Getter<Guild, EvtTextTopic>() {
            @Override
            public Guild get(@NotNull EvtTextTopic event) {
                return ((TextChannel) event.getJDAEvent().getChannel()).getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextTopic.class, Bot.class, new Getter<Bot, EvtTextTopic>() {
            @Override
            public Bot get(@NotNull EvtTextTopic event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtTextTopic extends SimpleDiSkyEvent<ChannelUpdateTopicEvent> {
        public EvtTextTopic(TextTopic event) { }
    }

}