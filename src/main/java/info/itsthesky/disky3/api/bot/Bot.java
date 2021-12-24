package info.itsthesky.disky3.api.bot;

import ch.njol.skript.util.Timespan;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ISnowflake;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Bot implements Comparable<Bot>, ISnowflake {

    private JDA core;
    private String name;
    private final @Nullable BotApplication application;
    private final long uptime;
    private NodeInformation node;

    public Bot() {
        this.uptime = System.currentTimeMillis();
        this.core = null;
        this.name = null;
        this.application = null;
    }

    public Bot(JDA core, String name, @Nullable BotApplication application) {
        this.uptime = System.currentTimeMillis();
        this.core = core;
        this.name = name;
        this.application = application;
    }

    public boolean hasApplication() {
        return application != null;
    }

    public @Nullable BotApplication getApplication() {
        return application;
    }

    public Timespan getUptime() {
        return new Timespan(System.currentTimeMillis() - uptime);
    }

    private void load() {
        core = BotManager.getLoadedBots().get(0).getCore();
        name = BotManager.getLoadedBots().get(0).getName();
    }

    public JDA getCore() {
       node = new NodeInformation();
        if (!BotManager.anyBotLoaded() && core == null) {
            DiSky.exception(new DiSkyException("Tried to get a bot but there arent any bot loaded in the server"), node);
        }
        if (core == null)
            load();
        return core;
    }

    public String getName() {
        node = new NodeInformation();
        if (!BotManager.anyBotLoaded() && name == null)
            DiSky.exception(new DiSkyException("Tried to get a bot but there arent any bot loaded in the server."), node);
        if (name == null)
            load();
        return name;
    }

    @Override
    public int compareTo(@NotNull Bot first) {
        if (first.getName().equalsIgnoreCase(getName()))
            return 0;
        return -1;
    }

    public void shutdown() {
        BotManager.remove(this);
    }

    @Override
    public long getIdLong() {
        return getCore().getSelfUser().getIdLong();
    }}