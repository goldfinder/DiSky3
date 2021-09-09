package info.itsthesky.disky3.core.skript.properties.guild;

import info.itsthesky.disky3.api.emojis.Emote;
import info.itsthesky.disky3.api.skript.properties.MultipleGuildProperty;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

public class GuildEmotes extends MultipleGuildProperty<Emote> {

    static {
        register(
                GuildEmotes.class,
                Emote.class,
                "[discord] emo(te|ji)[s]",
                "guild"
        );
    }

    @Override
    public @NotNull Emote[] converting(Guild guild) {
        return Emote.convert(guild.getEmotes());
    }

}
