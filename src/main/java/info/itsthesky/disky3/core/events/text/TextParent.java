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

public class TextParent extends DiSkyEvent<ChannelUpdateParentEvent> {

    @Override
    protected Predicate<ChannelUpdateParentEvent> checker() {
        return e -> e.isFromType(ChannelType.TEXT);
    }

    static {
        DiSkyEvent.register("Inner Event Name", TextParent.class, EvtTextParent.class,
                "[discord] text [channel] parent (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtTextParent.class, Category.class, new Getter<Category, EvtTextParent>() {
            @Override
            public Category get(@NotNull EvtTextParent event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 1);

       EventValues.registerEventValue(EvtTextParent.class, Category.class, new Getter<Category, EvtTextParent>() {
            @Override
            public Category get(@NotNull EvtTextParent event) {
                return event.getJDAEvent().getOldValue();
            }
        }, -1);

       EventValues.registerEventValue(EvtTextParent.class, TextChannel.class, new Getter<TextChannel, EvtTextParent>() {
            @Override
            public TextChannel get(@NotNull EvtTextParent event) {
                return (TextChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextParent.class, Guild.class, new Getter<Guild, EvtTextParent>() {
            @Override
            public Guild get(@NotNull EvtTextParent event) {
                return ((TextChannel) event.getJDAEvent().getChannel()).getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextParent.class, Bot.class, new Getter<Bot, EvtTextParent>() {
            @Override
            public Bot get(@NotNull EvtTextParent event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtTextParent extends SimpleDiSkyEvent<ChannelUpdateParentEvent> {
        public EvtTextParent(TextParent event) { }
    }

}