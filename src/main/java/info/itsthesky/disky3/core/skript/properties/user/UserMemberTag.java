package info.itsthesky.disky3.core.skript.properties.user;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Configuration;
import info.itsthesky.disky3.api.skript.properties.UserMemberProperty;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.User;

@Name("User Tag")
@Description("Get the discord tag / discriminator (four number after its #) of the user.")
@Since("3.0")
@Examples("discord tag of event-user")
public class UserMemberTag extends UserMemberProperty<String> {

    static {
        final String pattern;
        if (Configuration.PARSING_TAG_PROPERTY.get()) {
            pattern = "discriminator";
            DiSky.warn("Using custom pattern for the tag property! ('tag|discriminator' to 'discriminator' only)");
        } else {
            pattern = "(tag|discriminator)";
        }
        register(
                UserMemberTag.class,
                Object.class,
                "[discord] [user] " + pattern
        );
    }

    @Override
    public String converting(User original) {
        return original.getDiscriminator();
    }

}
