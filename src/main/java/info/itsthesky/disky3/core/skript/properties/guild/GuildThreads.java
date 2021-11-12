package info.itsthesky.disky3.core.skript.properties.guild;

import info.itsthesky.disky3.api.skript.properties.MultipleGuildProperty;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ThreadChannel;
import net.dv8tion.jda.api.entities.NewsChannel;
import org.jetbrains.annotations.NotNull;

public class GuildThreads extends MultipleGuildProperty<ThreadChannel> {

    static {
        register(
                GuildThreads.class,
                ThreadChannel.class,
                "[discord] threads",
                "guild"
        );
    }

    @Override
    public @NotNull ThreadChannel[] converting(Guild guild) {
        return guild.getThreadChannels().toArray(new ThreadChannel[0]);
    }

    @Override
    public @NotNull Class<? extends ThreadChannel> getReturnType() {
        return ThreadChannel.class;
    }
}
