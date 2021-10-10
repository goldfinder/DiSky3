package info.itsthesky.disky3.core.skript.properties.guild;

import info.itsthesky.disky3.api.skript.properties.MultipleGuildProperty;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildThread;
import net.dv8tion.jda.api.entities.NewsChannel;
import org.jetbrains.annotations.NotNull;

public class GuildThreads extends MultipleGuildProperty<GuildThread> {

    static {
        register(
                GuildThreads.class,
                GuildThread.class,
                "[discord] threads",
                "guild"
        );
    }

    @Override
    public @NotNull GuildThread[] converting(Guild guild) {
        return guild.getGuildThreads().toArray(new GuildThread[0]);
    }

    @Override
    public @NotNull Class<? extends GuildThread> getReturnType() {
        return GuildThread.class;
    }
}
