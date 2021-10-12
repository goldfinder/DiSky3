package info.itsthesky.disky3.core.events.voice;

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

public class VoiceParent extends DiSkyEvent<ChannelUpdateParentEvent> {

    @Override
    protected Predicate<ChannelUpdateParentEvent> checker() {
        return e -> e.isFromType(ChannelType.VOICE);
    }

    static {
        DiSkyEvent.register("Inner Event Name", VoiceParent.class, EvtVoiceParent.class,
                "[discord] voice [channel] parent (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtVoiceParent.class, Category.class, new Getter<Category, EvtVoiceParent>() {
            @Override
            public Category get(@NotNull EvtVoiceParent event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 1);

       EventValues.registerEventValue(EvtVoiceParent.class, Category.class, new Getter<Category, EvtVoiceParent>() {
            @Override
            public Category get(@NotNull EvtVoiceParent event) {
                return event.getJDAEvent().getOldValue();
            }
        }, -1);

       EventValues.registerEventValue(EvtVoiceParent.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceParent>() {
            @Override
            public VoiceChannel get(@NotNull EvtVoiceParent event) {
                return ((VoiceChannel) event.getJDAEvent().getChannel());
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceParent.class, Guild.class, new Getter<Guild, EvtVoiceParent>() {
            @Override
            public Guild get(@NotNull EvtVoiceParent event) {
                return ((VoiceChannel) event.getJDAEvent().getChannel()).getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceParent.class, Bot.class, new Getter<Bot, EvtVoiceParent>() {
            @Override
            public Bot get(@NotNull EvtVoiceParent event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtVoiceParent extends SimpleDiSkyEvent<ChannelUpdateParentEvent> {
        public EvtVoiceParent(VoiceParent event) { }
    }

}