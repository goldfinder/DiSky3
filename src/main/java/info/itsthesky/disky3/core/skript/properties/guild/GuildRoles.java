package info.itsthesky.disky3.core.skript.properties.guild;

import info.itsthesky.disky3.api.skript.properties.GuildProperty;
import info.itsthesky.disky3.api.skript.properties.MultipleGuildProperty;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

public class GuildRoles extends MultipleGuildProperty<Role> {

    static {
        register(
                GuildRoles.class,
                Role.class,
                "[discord] roles",
                "guild"
        );
    }

    @Override
    public @NotNull Role[] converting(Guild guild) {
        return guild.getRoles().toArray(new Role[0]);
    }


    @Override
    public @NotNull Class<? extends Role> getReturnType() {
        return Role.class;
    }
}
