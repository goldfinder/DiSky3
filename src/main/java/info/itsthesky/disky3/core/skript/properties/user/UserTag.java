package info.itsthesky.disky3.core.skript.properties.user;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.skript.properties.UserProperty;
import net.dv8tion.jda.api.entities.User;

@Name("User Tag")
@Description("Get the discord tag / discriminator (four number after its #) of the user.")
@Since("3.0")
@Examples("discord tag of event-user")
public class UserTag extends UserProperty<String> {

    static {
        register(
                UserTag.class,
                User.class,
                "[discord] [user] (tag|discriminator)"
        );
    }

    @Override
    public String converting(User original) {
        return original.getDiscriminator();
    }

}
