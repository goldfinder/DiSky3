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
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent;
import org.jetbrains.annotations.NotNull;

public class EmoteRemove extends DiSkyEvent<EmoteRemovedEvent> {

    static {
        DiSkyEvent.register("Emote Remove", EmoteRemove.class, EvtEmoteRemove.class,
                        "[guild] emo(ji|te) remove[d]")
                .setName("Emote Remove")
                .setDesc("Fired when a member remove an emote from a guild.",
                        "This is a log event, mean there's an event-author available.")
                .setExample("on emote remove:");

        EventValues.registerEventValue(EvtEmoteRemove.class, Guild.class, new Getter<Guild, EvtEmoteRemove>() {
            @Override
            public Guild get(@NotNull EmoteRemove.EvtEmoteRemove event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtEmoteRemove.class, Emote.class, new Getter<Emote, EvtEmoteRemove>() {
            @Override
            public Emote get(@NotNull EmoteRemove.EvtEmoteRemove event) {
                return Emote.fromJDA(event.getJDAEvent().getEmote());
            }
        }, 0);

        EventValues.registerEventValue(EvtEmoteRemove.class, JDA.class, new Getter<JDA, EvtEmoteRemove>() {
            @Override
            public JDA get(@NotNull EmoteRemove.EvtEmoteRemove event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtEmoteRemove extends SimpleDiSkyEvent<EmoteRemovedEvent> implements LogEvent {
        public EvtEmoteRemove(EmoteRemove event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}