package info.itsthesky.disky3.core.skript.properties.invite;

import info.itsthesky.disky3.api.skript.properties.InviteProperty;
import net.dv8tion.jda.api.entities.Invite;

public class InviteCode extends InviteProperty<String> {

    static {
        register(
                InviteCode.class,
                String.class,
                "[discord] [invite] code"
        );
    }

    @Override
    public String converting(Invite original) {
        return original.getCode();
    }
}
