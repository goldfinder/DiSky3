package info.itsthesky.disky3.api.skript.properties;

import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Same as {@link CustomProperty} but for multiple elements
 * @author ItsTheSky
 */
public abstract class MultipleCustomProperty<F, T> extends MultiplyPropertyExpression<F, T> {

    private static String propertyString;
    private static Class<?> propertyClass;

    protected static void register(
            Class itemClass,
            Class<?> propertyClazz,
            String property
    ) {
        propertyString = property;
        propertyClass = propertyClazz;
        register(
                itemClass,
                propertyClass,
                property,
                getFromType()
        );
    }

    protected static String getFromType() {
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return propertyString;
    }

    public abstract T[] converting(F original);

    @Override
    public @NotNull T[] convert(@NotNull F updatingMessage) {
        return converting(updatingMessage);
    }

    @Override
    public @NotNull Class<? extends T> getReturnType() {
        return (Class<T>) propertyClass;
    }
}
