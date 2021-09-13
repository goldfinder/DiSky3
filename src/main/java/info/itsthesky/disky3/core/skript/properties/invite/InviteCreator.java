package info.itsthesky.disky3.core.skript.properties.invite;

import info.itsthesky.disky3.api.skript.properties.InviteProperty;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.User;

public class InviteCreator extends InviteProperty<User> {

    static {
        register(
                InviteCreator.class,
                User.class,
                "[discord] [user] (creator|inviter|user)"
        );
    }

    @Override
    public User converting(Invite original) {
        return original.getInviter();
    }
}
