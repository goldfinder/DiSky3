package info.itsthesky.disky3.core.skript.properties.user;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.skript.properties.MultipleUserMemberProperty;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Name("User Badges")
@Description("Get every badges the user has, except Nitro & Nitro boost one.")
@Since("3.0")
@Examples("badges of event-user")
public class UserBadges extends MultipleUserMemberProperty<String> {

    static {
        register(
                UserBadges.class,
                String.class,
                "[discord] [user] badge[s]"
        );
    }

    @Override
    public String[] converting(User original) {
        return (original == null ? new ArrayList<User.UserFlag>() : original.getFlags()).stream().map(User.UserFlag::getName).toArray(String[]::new);
    }

}
