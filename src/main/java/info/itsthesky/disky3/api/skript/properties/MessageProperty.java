package info.itsthesky.disky3.api.skript.properties;

import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.properties.base.CustomProperty;
import org.jetbrains.annotations.NotNull;

/**
 * @author ItsTheSky
 */
public abstract class MessageProperty<T> extends CustomProperty<UpdatingMessage, T> {

    @Override
    protected @NotNull String getPropertyName() {
        return "message";
    }

    static {
        setFromType("message");
    }
}
