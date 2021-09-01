package info.itsthesky.disky3.core.events.text;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateTopicEvent;

public class TextTopic extends DiSkyEvent<TextChannelUpdateTopicEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", TextTopic.class, EvtTextTopic.class,
                "[discord] text [channel] topic (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtTextTopic.class, String.class, new Getter<String, EvtTextTopic>() {
            @Override
            public String get(EvtTextTopic event) {
                return event.getJDAEvent().getOldTopic();
            }
        }, -1);

       EventValues.registerEventValue(EvtTextTopic.class, String.class, new Getter<String, EvtTextTopic>() {
            @Override
            public String get(EvtTextTopic event) {
                return event.getJDAEvent().getNewTopic();
            }
        }, 1);

       EventValues.registerEventValue(EvtTextTopic.class, TextChannel.class, new Getter<TextChannel, EvtTextTopic>() {
            @Override
            public TextChannel get(EvtTextTopic event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextTopic.class, Guild.class, new Getter<Guild, EvtTextTopic>() {
            @Override
            public Guild get(EvtTextTopic event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextTopic.class, Bot.class, new Getter<Bot, EvtTextTopic>() {
            @Override
            public Bot get(EvtTextTopic event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtTextTopic extends SimpleDiSkyEvent<TextChannelUpdateTopicEvent> {
        public EvtTextTopic(TextTopic event) { }
    }

}