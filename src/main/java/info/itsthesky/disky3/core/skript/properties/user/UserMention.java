package info.itsthesky.disky3.core.skript.properties.user;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.skript.properties.UserProperty;
import net.dv8tion.jda.api.entities.User;

@Name("User Mention")
@Description("Get the mention tag of the user.")
@Since("3.0")
@Examples("mention tag of event-user")
public class UserMention extends UserProperty<String> {

    static {
        register(
                UserMention.class,
                User.class,
                "[discord] [user] mention [tag]"
        );
    }

    @Override
    public String converting(User original) {
        return original.getAsMention();
    }

}
