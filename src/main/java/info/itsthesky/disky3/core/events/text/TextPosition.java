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

public class TextPosition extends DiSkyEvent<ChannelUpdatePositionEvent> {

    @Override
    protected Predicate<ChannelUpdatePositionEvent> checker() {
        return e -> e.isFromType(ChannelType.TEXT);
    }

    static {
        DiSkyEvent.register("Text Position Change", TextPosition.class, EvtTextPosition.class,
                "[discord] text [channel] position (update|change)")
                .setName("Text Channel Position Change")
                .setDesc("Fired when the position of a text channel changes.")
                .setExample("on text channel position change:");


       EventValues.registerEventValue(EvtTextPosition.class, TextChannel.class, new Getter<TextChannel, EvtTextPosition>() {
            @Override
            public TextChannel get(@NotNull EvtTextPosition event) {
                return (TextChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextPosition.class, Guild.class, new Getter<Guild, EvtTextPosition>() {
            @Override
            public Guild get(@NotNull EvtTextPosition event) {
                return ((TextChannel) event.getJDAEvent().getChannel()).getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextPosition.class, Bot.class, new Getter<Bot, EvtTextPosition>() {
            @Override
            public Bot get(@NotNull EvtTextPosition event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtTextPosition extends SimpleDiSkyEvent<ChannelUpdatePositionEvent> {
        public EvtTextPosition(TextPosition event) { }
    }

}