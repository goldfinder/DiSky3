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
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent;
import org.jetbrains.annotations.NotNull;

public class EmoteUpdateName extends DiSkyEvent<EmoteUpdateNameEvent> {

    static {
        DiSkyEvent.register("Emote Name Update", EmoteUpdateName.class, EvtEmoteUpdateName.class,
                        "[guild] emo(ji|te) name (change|update)[d]")
                .setName("Emote Name Update")
                .setDesc("Fired when a member change the name of an emote (from a guild).",
                        "You can get both values, as past event-string and future event-string",
                        "This is a log event, mean there's an event-author available.")
                .setExample("on emote name update:");

        EventValues.registerEventValue(EvtEmoteUpdateName.class, Guild.class, new Getter<Guild, EvtEmoteUpdateName>() {
            @Override
            public Guild get(@NotNull EmoteUpdateName.EvtEmoteUpdateName event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtEmoteUpdateName.class, String.class, new Getter<String, EvtEmoteUpdateName>() {
            @Override
            public String get(@NotNull EmoteUpdateName.EvtEmoteUpdateName event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 0);

        EventValues.registerEventValue(EvtEmoteUpdateName.class, String.class, new Getter<String, EvtEmoteUpdateName>() {
            @Override
            public String get(@NotNull EmoteUpdateName.EvtEmoteUpdateName event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 1);

        EventValues.registerEventValue(EvtEmoteUpdateName.class, String.class, new Getter<String, EvtEmoteUpdateName>() {
            @Override
            public String get(@NotNull EmoteUpdateName.EvtEmoteUpdateName event) {
                return event.getJDAEvent().getOldValue();
            }
        }, -1);

        EventValues.registerEventValue(EvtEmoteUpdateName.class, Guild.class, new Getter<Guild, EvtEmoteUpdateName>() {
            @Override
            public Guild get(@NotNull EmoteUpdateName.EvtEmoteUpdateName event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtEmoteUpdateName.class, Emote.class, new Getter<Emote, EvtEmoteUpdateName>() {
            @Override
            public Emote get(@NotNull EmoteUpdateName.EvtEmoteUpdateName event) {
                return Emote.fromJDA(event.getJDAEvent().getEmote());
            }
        }, 0);

        EventValues.registerEventValue(EvtEmoteUpdateName.class, JDA.class, new Getter<JDA, EvtEmoteUpdateName>() {
            @Override
            public JDA get(@NotNull EmoteUpdateName.EvtEmoteUpdateName event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtEmoteUpdateName extends SimpleDiSkyEvent<EmoteUpdateNameEvent> implements LogEvent {
        public EvtEmoteUpdateName(EmoteUpdateName event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}