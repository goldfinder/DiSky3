package info.itsthesky.disky3.core.skript.properties.guild;

import info.itsthesky.disky3.api.skript.properties.MultipleGuildProperty;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.NewsChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

public class GuildNewsChannels extends MultipleGuildProperty<NewsChannel> {

    static {
        register(
                GuildNewsChannels.class,
                NewsChannel.class,
                "[discord] news( |-)channels",
                "guild"
        );
    }

    @Override
    public @NotNull NewsChannel[] converting(Guild guild) {
        return guild.getNewsChannels().toArray(new NewsChannel[0]);
    }

    @Override
    public @NotNull Class<? extends NewsChannel> getReturnType() {
        return NewsChannel.class;
    }
}
