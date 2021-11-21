package info.itsthesky.disky3.core.skript.properties.thread;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import info.itsthesky.disky3.api.skript.PropertyCondition;
import net.dv8tion.jda.api.entities.ThreadChannel;
import org.jetbrains.annotations.NotNull;

@Name("Thread Is Archived")
@Description({"Either a specific thread is archived or not."})
@Examples({"event-thread is archived",
        "thread with id \"000\" is not archived"})
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
