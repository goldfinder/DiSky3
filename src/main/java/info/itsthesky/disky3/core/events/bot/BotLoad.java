package info.itsthesky.disky3.core.events.bot;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import org.jetbrains.annotations.NotNull;

public class BotLoad extends DiSkyEvent<ReadyEvent> {

    static {
        EventValues.registerEventValue(EvtBotLoad.class, Bot.class, new Getter<Bot, EvtBotLoad>() {
            @Override
            public Bot get(@NotNull EvtBotLoad event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);
    }

    public static class EvtBotLoad extends SimpleDiSkyEvent<ReadyEvent> {
        public EvtBotLoad(BotLoad event) { }
    }

}