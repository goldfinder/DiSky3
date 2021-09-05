package info.itsthesky.disky3.api.skript.properties;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
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
