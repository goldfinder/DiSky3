package info.itsthesky.disky3.core.skript.properties.guild;

import info.itsthesky.disky3.api.skript.properties.MultipleGuildProperty;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.StageChannel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GuildChannels extends MultipleGuildProperty<GuildChannel> {

    static {
        register(
                GuildChannels.class,
                GuildChannel.class,
                "[discord] [guild( |-)]channels",
                "guild"
        );
    }

    @Override
    public @NotNull GuildChannel[] converting(Guild guild) {
        return guild.getChannels().toArray(new GuildChannel[0]);
    }

    @Override
    public @NotNull Class<? extends GuildChannel> getReturnType() {
        return GuildChannel.class;
    }
}