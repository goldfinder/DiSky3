package info.itsthesky.disky3.api.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ISnowflake;
import org.jetbrains.annotations.NotNull;

public class Bot implements Comparable<Bot>, ISnowflake {

    private final JDA core;
    private final String name;

    public Bot(JDA core, String name) {
        this.core = core;
        this.name = name;
    }

    public JDA getCore() {
        return core;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(@NotNull Bot first) {
        if (first.getName().equalsIgnoreCase(getName()))
            return 0;
        return -1;
    }

    @Override
    public long getIdLong() {
        return getCore().getSelfUser().getIdLong();
    }
}
