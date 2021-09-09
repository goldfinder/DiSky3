package info.itsthesky.disky3.core.skript.properties.guild;

import info.itsthesky.disky3.api.skript.properties.GuildProperty;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

public class GuildEveryone extends GuildProperty<Role> {

    static {
        register(
                GuildEveryone.class,
                Role.class,
                "[discord] (public|everyone) role",
                "guild"
        );
    }

    @Override
    public @NotNull Role converting(Guild guild) {
        return guild.getPublicRole();
    }

}
