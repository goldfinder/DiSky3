package info.itsthesky.disky3.api.skript.properties.base;

import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Same as {@link CustomProperty} but for multiple elements
 * @author ItsTheSky
 */
public abstract class MultipleCustomProperty<F, T> extends MultiplyPropertyExpression<F, T> {

    private static String propertyString;
    private static Class<?> propertyClass;
    private static String fromType;

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
                fromType
        );
    }

    protected static void setFromType(String s) {
        fromType = s;
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
