package info.itsthesky.disky3.core.skript.properties.thread;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import info.itsthesky.disky3.api.skript.PropertyCondition;
import net.dv8tion.jda.api.entities.ThreadChannel;
import org.jetbrains.annotations.NotNull;

@Name("Thread Is Locked")
@Description({"Either a specific thread is locked or not."})
@Examples({"event-thread is locked",
        "thread with id \"000\" is not locked"})
public class ThreadIsLocked extends PropertyCondition<ThreadChannel> {

    static {
        register(
                ThreadIsLocked.class,
                PropertyType.BE,
                "[been] lock[ed]",
                "thread"
        );
    }

    @Override
    public boolean check(@NotNull ThreadChannel thread) {
        return thread.isLocked();
    }

    @Override
    protected String getPropertyName() {
        return "locked";
    }
}
