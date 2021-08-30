package info.itsthesky.disky3.core.skript.properties.user;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.skript.properties.UserProperty;
import net.dv8tion.jda.api.entities.User;

@Name("User ID")
@Description("Get the unique ID of the user.")
@Since("3.0")
@Examples("discord id of event-user")
public class UserID extends UserProperty<String> {

    static {
        register(
                UserID.class,
                User.class,
                "discord [user] id"
        );
    }

    @Override
    public String converting(User original) {
        return original.getId();
    }

}
