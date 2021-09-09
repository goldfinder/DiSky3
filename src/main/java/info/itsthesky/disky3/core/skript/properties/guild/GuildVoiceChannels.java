package info.itsthesky.disky3.core.skript.properties.guild;

import info.itsthesky.disky3.api.skript.properties.MultipleGuildProperty;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.jetbrains.annotations.NotNull;

public class GuildVoiceChannels extends MultipleGuildProperty<VoiceChannel> {

    static {
        register(
                GuildVoiceChannels.class,
                VoiceChannel.class,
                "[discord] voice( |-)channel[s]",
                "guild"
        );
    }

    @Override
    public @NotNull VoiceChannel[] converting(Guild guild) {
        return guild.getVoiceChannels().toArray(new VoiceChannel[0]);
    }

}
