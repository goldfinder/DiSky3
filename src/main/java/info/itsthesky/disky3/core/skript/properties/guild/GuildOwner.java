package info.itsthesky.disky3.core.skript.properties.guild;

import info.itsthesky.disky3.api.skript.properties.GuildProperty;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

public class GuildOwner extends GuildProperty<Member> {

    static {
        register(
                GuildOwner.class,
                Member.class,
                "[discord] owner",
                "guild"
        );
    }

    @Override
    public @NotNull Member converting(Guild guild) {
        return guild.getOwner();
    }

}
