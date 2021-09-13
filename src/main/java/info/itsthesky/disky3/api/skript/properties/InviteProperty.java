package info.itsthesky.disky3.api.skript.properties;

import info.itsthesky.disky3.api.skript.properties.base.CustomProperty;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import org.jetbrains.annotations.NotNull;

/**
 * @author ItsTheSky
 */
public abstract class InviteProperty<T> extends CustomProperty<Invite, T> {

    @Override
    protected @NotNull String getPropertyName() {
        return "invite";
    }

    static {
        setFromType("invite");
    }
}
