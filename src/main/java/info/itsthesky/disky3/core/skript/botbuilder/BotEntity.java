package info.itsthesky.disky3.core.skript.botbuilder;

import ch.njol.skript.lang.TriggerItem;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotApplication;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.event.Event;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

/**
 * Object that represent a defined scope of a bot, containing bot itself, code to run in some events and more.
 */
public class BotEntity {

    private final String name;
    private final String token;
    private final BotApplication application;
    private final List<GatewayIntent> enableIntents;
    private final List<TriggerItem> onReady;
    private final List<TriggerItem> onGuildReady;

    public BotEntity(String name, String token, BotApplication application, List<GatewayIntent> enableIntents, List<TriggerItem> onReady, List<TriggerItem> onGuildReady) {
        this.name = name;
        this.application = application;
        this.token = token;
        this.enableIntents = enableIntents;
        this.onReady = onReady;
        this.onGuildReady = onGuildReady;
    }

    public static BotEntity empty(String name) {
        return new BotEntity(name, null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public Bot build() {
        final JDA core;
        try {
            core = JDABuilder
                    .create(getToken(), getEnableIntents())
                    .addEventListeners(new BotListener(this))
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
            return null;
        }
        return new Bot(core, getName(), getApplication());
    }

    private void execute(List<TriggerItem> items, Event e) {
        if (items.isEmpty())
            return;
        TriggerItem.walk(items.get(0), e);
    }

    public void onReady(Event e) {
        execute(getOnReady(), e);
    }

    public void onGuildReady(Event e) {
        execute(getOnGuildReady(), e);
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public BotApplication getApplication() {
        return application;
    }

    public List<GatewayIntent> getEnableIntents() {
        return enableIntents;
    }

    public List<TriggerItem> getOnReady() {
        return onReady;
    }

    public List<TriggerItem> getOnGuildReady() {
        return onGuildReady;
    }
}
