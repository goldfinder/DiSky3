package info.itsthesky.disky3.core.events.bot;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;

public class BotLeave extends DiSkyEvent<GuildLeaveEvent> {

    static {
        DiSkyEvent.register("Bot Leave Guild", BotLeave.class, EvtBotLeave.class,
                "bot leave [guild]")
                .setName("Bot Leave Guild");


       EventValues.registerEventValue(EvtBotLeave.class, Guild.class, new Getter<Guild, EvtBotLeave>() {
            @Override
            public Guild get(EvtBotLeave event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtBotLeave.class, Bot.class, new Getter<Bot, EvtBotLeave>() {
            @Override
            public Bot get(EvtBotLeave event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtBotLeave extends SimpleDiSkyEvent<GuildLeaveEvent> {
        public EvtBotLeave(BotLeave event) { }
    }

}