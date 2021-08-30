package info.itsthesky.disky3.api.skript.properties;

import info.itsthesky.disky3.api.messages.UpdatingMessage;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

/**
 * @author ItsTheSky
 */
public abstract class UserProperty<T> extends CustomProperty<User, T> {

    @Override
    protected @NotNull String getPropertyName() {
        return "user";
    }

}
