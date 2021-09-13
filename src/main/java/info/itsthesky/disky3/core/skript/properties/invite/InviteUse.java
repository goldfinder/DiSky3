package info.itsthesky.disky3.core.skript.properties.invite;

import info.itsthesky.disky3.api.skript.properties.InviteProperty;
import net.dv8tion.jda.api.entities.Invite;

public class InviteUse extends InviteProperty<Number> {

    static {
        register(
                InviteUse.class,
                Number.class,
                "[discord] [user] us(age|e)[s]"
        );
    }

    @Override
    public Number converting(Invite original) {
        return original.getUses();
    }
}
