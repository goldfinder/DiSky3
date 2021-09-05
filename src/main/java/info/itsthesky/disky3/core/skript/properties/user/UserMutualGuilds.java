package info.itsthesky.disky3.core.skript.properties.user;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.skript.properties.MultipleUserMemberProperty;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

@Name("User Mutual Guilds")
@Description("Get the mutual guilds of the user.")
@Since("3.0")
@Examples("mutual guilds of event-user")
public class UserMutualGuilds extends MultipleUserMemberProperty<Guild> {

    static {
        register(
                UserMutualGuilds.class,
                Guild.class,
                "[discord] [user] mutual[s] guild[s]"
        );
    }

    @Override
    public Guild[] converting(User original) {
        return original.getMutualGuilds().toArray(new Guild[0]);
    }

}
