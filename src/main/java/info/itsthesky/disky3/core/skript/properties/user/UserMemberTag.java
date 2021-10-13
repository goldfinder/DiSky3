package info.itsthesky.disky3.core.skript.properties.user;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.Configuration;
import info.itsthesky.disky3.api.skript.properties.UserMemberProperty;
import net.dv8tion.jda.api.entities.User;

@Name("User Tag")
@Description("Get the discord tag / discriminator (four number after its #) of the user.")
@Since("3.0")
@Examples("discord tag of event-user")
public class UserMemberTag extends UserMemberProperty<String> {

    static {
        register(
                UserMemberTag.class,
                Object.class,
                "[discord] [user] " + (Configuration.PARSING_TAG_PROPERTY.get() ? "discriminator" : "(tag|discriminator)")
        );
    }

    @Override
    public String converting(User original) {
        return original.getDiscriminator();
    }

}
