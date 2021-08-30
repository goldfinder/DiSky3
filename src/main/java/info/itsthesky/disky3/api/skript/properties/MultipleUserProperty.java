package info.itsthesky.disky3.api.skript.properties;

import info.itsthesky.disky3.api.messages.UpdatingMessage;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

public abstract class MultipleUserProperty<T> extends MultipleCustomProperty<User, T> {

    @Override
    protected @NotNull String getPropertyName() {
        return "user";
    }
}
