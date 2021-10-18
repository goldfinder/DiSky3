package info.itsthesky.disky3.core.events.guild;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBannerEvent;
import org.jetbrains.annotations.NotNull;

public class GuildBanner extends DiSkyEvent<GuildUpdateBannerEvent> {

    static {
        DiSkyEvent.register("Guild Banner Change", GuildBanner.class, EvtGuildBanner.class,
                "[discord] [guild] banner (update|change)]")
                .setName("Guild Banner Change")
                .setDesc("Fired when the banner of a guild changes.")
                .setExample("on guild banner change:");


        EventValues.registerEventValue(EvtGuildBanner.class, String.class, new Getter<String, EvtGuildBanner>() {
            @Override
            public String get(@NotNull EvtGuildBanner event) {
                return event.getJDAEvent().getNewBannerUrl();
            }
        }, 1);

        EventValues.registerEventValue(EvtGuildBanner.class, String.class, new Getter<String, EvtGuildBanner>() {
            @Override
            public String get(@NotNull EvtGuildBanner event) {
                return event.getJDAEvent().getOldBannerUrl();
            }
        }, -1);

        EventValues.registerEventValue(EvtGuildBanner.class, String.class, new Getter<String, EvtGuildBanner>() {
            @Override
            public String get(@NotNull EvtGuildBanner event) {
                return event.getJDAEvent().getOldBannerUrl();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildBanner.class, Guild.class, new Getter<Guild, EvtGuildBanner>() {
            @Override
            public Guild get(@NotNull EvtGuildBanner event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildBanner.class, Bot.class, new Getter<Bot, EvtGuildBanner>() {
            @Override
            public Bot get(@NotNull EvtGuildBanner event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtGuildBanner extends SimpleDiSkyEvent<GuildUpdateBannerEvent> {
        public EvtGuildBanner(GuildBanner event) { }
    }

}