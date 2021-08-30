package info.itsthesky.disky3.api.skript.properties;

import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import org.jetbrains.annotations.NotNull;

public abstract class MultipleMessageProperty<T> extends MultipleCustomProperty<UpdatingMessage, T> {

    @Override
    protected @NotNull String getPropertyName() {
        return "message";
    }
}
