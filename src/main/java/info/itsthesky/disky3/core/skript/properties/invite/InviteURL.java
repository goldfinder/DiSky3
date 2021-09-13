package info.itsthesky.disky3.core.skript.properties.invite;

import info.itsthesky.disky3.api.skript.properties.InviteProperty;
import net.dv8tion.jda.api.entities.Invite;

public class InviteURL extends InviteProperty<String> {

    static {
        register(
                InviteURL.class,
                String.class,
                "[discord] [invite] ur(l|i)"
        );
    }

    @Override
    public String converting(Invite original) {
        return original.getUrl();
    }
}
