package info.itsthesky.disky3.core.skript.properties.thread;

import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.PropertyCondition;
import net.dv8tion.jda.api.entities.GuildThread;
import org.jetbrains.annotations.NotNull;

public class ThreadIsArchived extends PropertyCondition<GuildThread> {

    static {
        register(
                ThreadIsArchived.class,
                PropertyType.BE,
                "[been] archive[d]",
                "thread"
        );
    }

    @Override
    public boolean check(@NotNull GuildThread thread) {
        return thread.isArchived();
    }

    @Override
    protected String getPropertyName() {
        return "archived";
    }
}
