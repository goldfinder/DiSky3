package info.itsthesky.disky3.core.events.guild;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostTierEvent;

import java.util.Locale;

public class GuildBoostTier extends DiSkyEvent<GuildUpdateBoostTierEvent> {

    static {
        DiSkyEvent.register("Guild Boost Tier Update", GuildBoostTier.class, EvtGuildBoostTier.class,
                "[guild] boost tier (update|change)")
                .setName("Guild Boost Tier Update");

       EventValues.registerEventValue(EvtGuildBoostTier.class, String.class, new Getter<String, EvtGuildBoostTier>() {
            @Override
            public String get(EvtGuildBoostTier event) {
                return event.getJDAEvent().getNewBoostTier().name().toLowerCase(Locale.ROOT).replace("_", "");
            }
        }, 1);

        EventValues.registerEventValue(EvtGuildBoostTier.class, String.class, new Getter<String, EvtGuildBoostTier>() {
            @Override
            public String get(EvtGuildBoostTier event) {
                return event.getJDAEvent().getOldBoostTier().name().toLowerCase(Locale.ROOT).replace("_", "");
            }
        }, -1);

        EventValues.registerEventValue(EvtGuildBoostTier.class, String.class, new Getter<String, EvtGuildBoostTier>() {
            @Override
            public String get(EvtGuildBoostTier event) {
                return event.getJDAEvent().getOldBoostTier().name().toLowerCase(Locale.ROOT).replace("_", "");
            }
        }, 0);

        EventValues.registerEventValue(EvtGuildBoostTier.class, String.class, new Getter<String, EvtGuildBoostTier>() {
            @Override
            public String get(EvtGuildBoostTier event) {
                return event.getJDAEvent().getOldBoostTier().name().toLowerCase(Locale.ROOT).replace("_", "");
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildBoostTier.class, Guild.class, new Getter<Guild, EvtGuildBoostTier>() {
            @Override
            public Guild get(EvtGuildBoostTier event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildBoostTier.class, Guild.class, new Getter<Guild, EvtGuildBoostTier>() {
            @Override
            public Guild get(EvtGuildBoostTier event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildBoostTier.class, Bot.class, new Getter<Bot, EvtGuildBoostTier>() {
            @Override
            public Bot get(EvtGuildBoostTier event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtGuildBoostTier extends SimpleDiSkyEvent<GuildUpdateBoostTierEvent> {
        public EvtGuildBoostTier(GuildBoostTier event) { }
    }

}