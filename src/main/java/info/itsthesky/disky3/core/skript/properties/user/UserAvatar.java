package info.itsthesky.disky3.core.skript.properties.user;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.skript.properties.UserProperty;
import net.dv8tion.jda.api.entities.User;

@Name("User Avatar")
@Description("Get the avatar URL of the user. If the user doesn't have any avatar, return the default avatar URL.")
@Since("3.0")
@Examples("avatar of event-user")
public class UserAvatar extends UserProperty<String> {

    static {
        register(
                UserAvatar.class,
                User.class,
                "[discord] [user] avatar"
        );
    }

    @Override
    public String converting(User original) {
        return original.getAvatarUrl() == null ? original.getDefaultAvatarUrl() : original.getAvatarUrl();
    }

}
