package info.itsthesky.disky3.core.events.text;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateSlowmodeEvent;

public class TextSlowmode extends DiSkyEvent<TextChannelUpdateSlowmodeEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", TextSlowmode.class, EvtTextSlowmode.class,
                "[discord] text [channel] slowmode (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtTextSlowmode.class, TextChannel.class, new Getter<TextChannel, EvtTextSlowmode>() {
            @Override
            public TextChannel get(EvtTextSlowmode event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextSlowmode.class, Guild.class, new Getter<Guild, EvtTextSlowmode>() {
            @Override
            public Guild get(EvtTextSlowmode event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextSlowmode.class, JDA.class, new Getter<JDA, EvtTextSlowmode>() {
            @Override
            public JDA get(EvtTextSlowmode event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtTextSlowmode extends SimpleDiSkyEvent<TextChannelUpdateSlowmodeEvent> {
        public EvtTextSlowmode(TextSlowmode event) { }
    }

}