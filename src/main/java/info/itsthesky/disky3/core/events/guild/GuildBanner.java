package info.itsthesky.disky3.core.events.guild;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBannerEvent;

public class GuildBanner extends DiSkyEvent<GuildUpdateBannerEvent> {

    static {
        DiSkyEvent.register("Guild Banner Update", GuildBanner.class, EvtGuildBanner.class,
                "[guild] banner (update|change)]")
                .setName("Guild Banner Update");


        EventValues.registerEventValue(EvtGuildBanner.class, String.class, new Getter<String, EvtGuildBanner>() {
            @Override
            public String get(EvtGuildBanner event) {
                return event.getJDAEvent().getNewBannerUrl();
            }
        }, 1);

        EventValues.registerEventValue(EvtGuildBanner.class, String.class, new Getter<String, EvtGuildBanner>() {
            @Override
            public String get(EvtGuildBanner event) {
                return event.getJDAEvent().getOldBannerUrl();
            }
        }, -1);

        EventValues.registerEventValue(EvtGuildBanner.class, String.class, new Getter<String, EvtGuildBanner>() {
            @Override
            public String get(EvtGuildBanner event) {
                return event.getJDAEvent().getOldBannerUrl();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildBanner.class, Guild.class, new Getter<Guild, EvtGuildBanner>() {
            @Override
            public Guild get(EvtGuildBanner event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildBanner.class, JDA.class, new Getter<JDA, EvtGuildBanner>() {
            @Override
            public JDA get(EvtGuildBanner event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtGuildBanner extends SimpleDiSkyEvent<GuildUpdateBannerEvent> {
        public EvtGuildBanner(GuildBanner event) { }
    }

}