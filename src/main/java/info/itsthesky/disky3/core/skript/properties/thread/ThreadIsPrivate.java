package info.itsthesky.disky3.core.skript.properties.thread;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import info.itsthesky.disky3.api.skript.PropertyCondition;
import net.dv8tion.jda.api.entities.ThreadChannel;
import org.jetbrains.annotations.NotNull;

@Name("Thread Is Private")
@Description({"Either a specific thread is private or not.",
        "Private thread are unlocked at the second level of the guild!"})
@Examples({"event-thread is private",
        "thread with id \"000\" is not private"})
public class ThreadIsPrivate extends PropertyCondition<ThreadChannel> {

    static {
        register(
                ThreadIsPrivate.class,
                PropertyType.BE,
                "[been] private",
                "thread"
        );
    }

    @Override
    public boolean check(@NotNull ThreadChannel thread) {
        return !thread.isPublic();
    }

    @Override
    protected String getPropertyName() {
        return "private";
    }
}
