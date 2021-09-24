package info.itsthesky.disky3.core.skript.properties.guild;

import info.itsthesky.disky3.api.skript.properties.MultipleGuildProperty;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

public class GuildTextChannels extends MultipleGuildProperty<TextChannel> {

    static {
        register(
                GuildTextChannels.class,
                TextChannel.class,
                "[discord] text( |-)channel[s]",
                "guild"
        );
    }

    @Override
    public @NotNull TextChannel[] converting(Guild guild) {
        return guild.getTextChannels().toArray(new TextChannel[0]);
    }

    @Override
    public @NotNull Class<? extends TextChannel> getReturnType() {
        return TextChannel.class;
    }
}
