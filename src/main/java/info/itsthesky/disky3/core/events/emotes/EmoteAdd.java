package info.itsthesky.disky3.core.events.emotes;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.emojis.Emote;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent;
import org.jetbrains.annotations.NotNull;

public class EmoteAdd extends DiSkyEvent<EmoteAddedEvent> {

    static {
        DiSkyEvent.register("Emote Add", EmoteAdd.class, EvtEmoteAdd.class,
                        "[guild] emo(ji|te) add[ed]")
                .setName("Emote Add")
                .setDesc("Fired when a member add a new emote in a guild.",
                        "This is a log event, mean there's an event-author available.")
                .setExample("on emote add:");

        EventValues.registerEventValue(EvtEmoteAdd.class, Guild.class, new Getter<Guild, EvtEmoteAdd>() {
            @Override
            public Guild get(@NotNull EmoteAdd.EvtEmoteAdd event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtEmoteAdd.class, Emote.class, new Getter<Emote, EvtEmoteAdd>() {
            @Override
            public Emote get(@NotNull EmoteAdd.EvtEmoteAdd event) {
                return Emote.fromJDA(event.getJDAEvent().getEmote());
            }
        }, 0);

        EventValues.registerEventValue(EvtEmoteAdd.class, JDA.class, new Getter<JDA, EvtEmoteAdd>() {
            @Override
            public JDA get(@NotNull EmoteAdd.EvtEmoteAdd event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtEmoteAdd extends SimpleDiSkyEvent<EmoteAddedEvent> implements LogEvent {
        public EvtEmoteAdd(EmoteAdd event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}