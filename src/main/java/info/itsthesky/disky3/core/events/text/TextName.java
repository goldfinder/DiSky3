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

public class TextName extends DiSkyEvent<ChannelUpdateNSFWEvent> {

    @Override
    protected Predicate<ChannelUpdateNSFWEvent> checker() {
        return e -> e.isFromType(ChannelType.TEXT);
    }

    static {
        DiSkyEvent.register("Inner Event Name", TextName.class, EvtTextName.class,
                "[discord] text [channel] name (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtTextName.class, TextChannel.class, new Getter<TextChannel, EvtTextName>() {
            @Override
            public TextChannel get(@NotNull EvtTextName event) {
                return (TextChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextName.class, Guild.class, new Getter<Guild, EvtTextName>() {
            @Override
            public Guild get(@NotNull EvtTextName event) {
                return ((TextChannel) event.getJDAEvent().getChannel()).getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextName.class, Bot.class, new Getter<Bot, EvtTextName>() {
            @Override
            public Bot get(@NotNull EvtTextName event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtTextName extends SimpleDiSkyEvent<ChannelUpdateNSFWEvent> {
        public EvtTextName(TextName event) { }
    }

}