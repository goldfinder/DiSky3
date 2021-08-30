package info.itsthesky.disky3.core.skript.properties.user;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.properties.MessageProperty;
import info.itsthesky.disky3.api.skript.properties.UserProperty;
import net.dv8tion.jda.api.entities.User;

@Name("User Name")
@Description("Get the discord name of the user.")
@Since("3.0")
@Examples("discord name of event-user")
public class UserName extends UserProperty<String> {

    static {
        register(
                UserName.class,
                User.class,
                "discord [user] name"
        );
    }

    @Override
    public String converting(User original) {
        return original.getName();
    }

}
