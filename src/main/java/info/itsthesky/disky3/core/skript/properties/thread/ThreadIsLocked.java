package info.itsthesky.disky3.core.skript.properties.thread;

import info.itsthesky.disky3.api.skript.PropertyCondition;
import net.dv8tion.jda.api.entities.GuildThread;
import org.jetbrains.annotations.NotNull;

public class ThreadIsLocked extends PropertyCondition<GuildThread> {

    static {
        register(
                ThreadIsLocked.class,
                PropertyType.BE,
                "[been] lock[ed]",
                "thread"
        );
    }

    @Override
    public boolean check(@NotNull GuildThread thread) {
        return thread.isLocked();
    }

    @Override
    protected String getPropertyName() {
        return "locked";
    }
}
