package info.itsthesky.disky3.core.events.guild;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import org.jetbrains.annotations.NotNull;

public class GuildReady extends DiSkyEvent<GuildReadyEvent> {
    static {
        DiSkyEvent.register("Guild Ready", GuildAFKChannel.class, GuildAFKChannel.EvtGuildAFKChannel.class,
                        "[discord] guild (ready|load)")
                .setName("Guild Load")
                .setDesc("Fired when a guild finishes loading and is ready to receive any events.")
                .setExample("on guild load:");


        EventValues.registerEventValue(EvtGuildReady.class, Guild.class, new Getter<Guild, EvtGuildReady>() {
            @Override
            public Guild get(@NotNull EvtGuildReady event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);
    }

    public static class EvtGuildReady extends SimpleDiSkyEvent<GuildReadyEvent> {
        public EvtGuildReady(GuildReady event) { }
    }

}