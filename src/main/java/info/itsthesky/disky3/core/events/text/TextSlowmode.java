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

public class TextSlowmode extends DiSkyEvent<ChannelUpdateSlowmodeEvent> {

    @Override
    protected Predicate<ChannelUpdateSlowmodeEvent> checker() {
        return e -> e.isFromType(ChannelType.TEXT);
    }

    static {
        DiSkyEvent.register("Text Slowmode Change", TextSlowmode.class, EvtTextSlowmode.class,
                "[discord] text [channel] slowmode (update|change)")
                .setName("Text Channel Slowmode Change")
                .setDesc("Fired when the slowmode of a text channel changes.")
                .setExample("on text slowmode change:");


       EventValues.registerEventValue(EvtTextSlowmode.class, TextChannel.class, new Getter<TextChannel, EvtTextSlowmode>() {
            @Override
            public TextChannel get(@NotNull EvtTextSlowmode event) {
                return (TextChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextSlowmode.class, Guild.class, new Getter<Guild, EvtTextSlowmode>() {
            @Override
            public Guild get(@NotNull EvtTextSlowmode event) {
                return ((TextChannel) event.getJDAEvent().getChannel()).getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextSlowmode.class, Bot.class, new Getter<Bot, EvtTextSlowmode>() {
            @Override
            public Bot get(@NotNull EvtTextSlowmode event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtTextSlowmode extends SimpleDiSkyEvent<ChannelUpdateSlowmodeEvent> {
        public EvtTextSlowmode(TextSlowmode event) { }
    }

}