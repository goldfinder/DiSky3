package info.itsthesky.disky3.core.events.voice;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateParentEvent;

public class VoiceParent extends DiSkyEvent<VoiceChannelUpdateParentEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", VoiceParent.class, EvtVoiceParent.class,
                "[discord] voice [channel] parent (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtVoiceParent.class, Category.class, new Getter<Category, EvtVoiceParent>() {
            @Override
            public Category get(EvtVoiceParent event) {
                return event.getJDAEvent().getNewParent();
            }
        }, 1);

       EventValues.registerEventValue(EvtVoiceParent.class, Category.class, new Getter<Category, EvtVoiceParent>() {
            @Override
            public Category get(EvtVoiceParent event) {
                return event.getJDAEvent().getOldParent();
            }
        }, -1);

       EventValues.registerEventValue(EvtVoiceParent.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceParent>() {
            @Override
            public VoiceChannel get(EvtVoiceParent event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceParent.class, Guild.class, new Getter<Guild, EvtVoiceParent>() {
            @Override
            public Guild get(EvtVoiceParent event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceParent.class, Bot.class, new Getter<Bot, EvtVoiceParent>() {
            @Override
            public Bot get(EvtVoiceParent event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtVoiceParent extends SimpleDiSkyEvent<VoiceChannelUpdateParentEvent> {
        public EvtVoiceParent(VoiceParent event) { }
    }

}