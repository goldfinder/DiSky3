package info.itsthesky.disky3.core.events.bot;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import org.jetbrains.annotations.NotNull;

public class BotJoin extends DiSkyEvent<GuildJoinEvent> {

    public static Invite usedInvite;

    static {
        DiSkyEvent.register("Bot Join Guild", BotJoin.class, EvtBotJoin.class,
                "bot join [guild]")
                .setName("Bot Join Guild")
                .setDesc("Fired when the bot joins into a guild.")
                .setExample("on bot join guild:");

       EventValues.registerEventValue(EvtBotJoin.class, Guild.class, new Getter<Guild, EvtBotJoin>() {
            @Override
            public Guild get(@NotNull EvtBotJoin event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtBotJoin.class, Bot.class, new Getter<Bot, EvtBotJoin>() {
            @Override
            public Bot get(@NotNull EvtBotJoin event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

        EventValues.registerEventValue(EvtBotJoin.class, Invite.class, new Getter<Invite, EvtBotJoin>() {
            @Override
            public Invite get(@NotNull EvtBotJoin event) {
                return usedInvite;
            }
        }, 0);

    }

    public static class EvtBotJoin extends SimpleDiSkyEvent<GuildJoinEvent> {
        public EvtBotJoin(BotJoin event) { }
    }

}