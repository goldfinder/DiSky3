package info.itsthesky.disky3.api;

import info.itsthesky.disky3.core.commands.Argument;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class StaticData {

    /**
     * Represent the last used argument to be synced with (future) section
     */
    public static List<Argument<?>> LAST_ARGUMENTS = new ArrayList<Argument<?>>();

    /**
     * Represent the last (nullable) event guild of a discord command execution
     */
    public static @Nullable Guild LAST_GUILD_COMMAND;

}
