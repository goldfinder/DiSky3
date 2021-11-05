package info.itsthesky.disky3.core.skript.properties.guild;

import info.itsthesky.disky3.api.skript.properties.MultipleGuildProperty;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.NewsChannel;
import net.dv8tion.jda.api.entities.StageChannel;
import org.jetbrains.annotations.NotNull;

public class GuildStageChannels extends MultipleGuildProperty<StageChannel> {

    static {
        register(
                GuildStageChannels.class,
                StageChannel.class,
                "[discord] stage( |-)channels",
                "guild"
        );
    }

    @Override
    public @NotNull StageChannel[] converting(Guild guild) {
        return guild.getStageChannels().toArray(new StageChannel[0]);
    }

    @Override
    public @NotNull Class<? extends StageChannel> getReturnType() {
        return StageChannel.class;
    }
}
