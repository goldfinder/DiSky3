package info.itsthesky.disky3.core.skript.properties.user;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.skript.properties.MultipleUserProperty;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

@Name("User Badges")
@Description("Get every badges the user has, except Nitro & Nitro boost one.")
@Since("3.0")
@Examples("badges of event-user")
public class UserBadges extends MultipleUserProperty<String> {

    static {
        register(
                UserBadges.class,
                String.class,
                "[discord] [user] badge[s]"
        );
    }

    @Override
    public String[] converting(User original) {
        return Utils.convertArray(
                User.UserFlag::getName,
                original.getFlags().toArray(new User.UserFlag[0])
        );
    }

}
