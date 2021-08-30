package info.itsthesky.disky3.core.events.text;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNSFWEvent;

public class TextNSFW extends DiSkyEvent<TextChannelUpdateNSFWEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", TextNSFW.class, EvtTextNSFW.class,
                "[discord] text [channel] nsfw (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtTextNSFW.class, TextChannel.class, new Getter<TextChannel, EvtTextNSFW>() {
            @Override
            public TextChannel get(EvtTextNSFW event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextNSFW.class, Guild.class, new Getter<Guild, EvtTextNSFW>() {
            @Override
            public Guild get(EvtTextNSFW event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextNSFW.class, JDA.class, new Getter<JDA, EvtTextNSFW>() {
            @Override
            public JDA get(EvtTextNSFW event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtTextNSFW extends SimpleDiSkyEvent<TextChannelUpdateNSFWEvent> {
        public EvtTextNSFW(TextNSFW event) { }
    }

}