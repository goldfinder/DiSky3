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

public class TextNSFW extends DiSkyEvent<ChannelUpdateNSFWEvent> {

    @Override
    protected Predicate<ChannelUpdateNSFWEvent> checker() {
        return e -> e.isFromType(ChannelType.TEXT);
    }

    static {
        DiSkyEvent.register("Text NSFW Change", TextNSFW.class, EvtTextNSFW.class,
                "[discord] text [channel] nsfw (update|change)")
                .setName("Text Channel NSFW Change")
                .setDesc("Fired when the nsfw state of a text channel changes.")
                .setExample("on text nsfw change:");

       EventValues.registerEventValue(EvtTextNSFW.class, TextChannel.class, new Getter<TextChannel, EvtTextNSFW>() {
            @Override
            public TextChannel get(@NotNull EvtTextNSFW event) {
                return (TextChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextNSFW.class, Guild.class, new Getter<Guild, EvtTextNSFW>() {
            @Override
            public Guild get(@NotNull EvtTextNSFW event) {
                return ((TextChannel) event.getJDAEvent().getChannel()).getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextNSFW.class, Bot.class, new Getter<Bot, EvtTextNSFW>() {
            @Override
            public Bot get(@NotNull EvtTextNSFW event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtTextNSFW extends SimpleDiSkyEvent<ChannelUpdateNSFWEvent> {
        public EvtTextNSFW(TextNSFW event) { }
    }

}