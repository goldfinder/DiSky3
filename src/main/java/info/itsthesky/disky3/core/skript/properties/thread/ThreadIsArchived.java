package info.itsthesky.disky3.core.skript.properties.thread;

import info.itsthesky.disky3.api.skript.PropertyCondition;
import net.dv8tion.jda.api.entities.ThreadChannel;
import org.jetbrains.annotations.NotNull;

public class ThreadIsArchived extends PropertyCondition<ThreadChannel> {

    static {
        register(
                ThreadIsArchived.class,
                PropertyType.BE,
                "[been] archive[d]",
                "thread"
        );
    }

    @Override
    public boolean check(@NotNull ThreadChannel thread) {
        return thread.isArchived();
    }

    @Override
    protected String getPropertyName() {
        return "archived";
    }
}
