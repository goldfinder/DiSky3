package info.itsthesky.disky3.core.skript.botbuilder;

import ch.njol.skript.lang.TriggerItem;
import info.itsthesky.disky3.core.events.bot.BotLoad;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Class that will listen to the two main event of the scope, onReady & onGuildReady
 */
public class BotListener extends ListenerAdapter {

    private final BotEntity instance;

    public BotListener(BotEntity instance) {
        this.instance = instance;
    }

    public BotEntity getInstance() {
        return instance;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        final BotLoad.EvtBotLoad e = new BotLoad.EvtBotLoad(new BotLoad());
        e.setJDAEvent(event);
        getInstance().onReady(e);
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {

    }
}
