package info.itsthesky.disky3.api.skript.properties;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import org.jetbrains.annotations.NotNull;

/**
 * @author ItsTheSky
 */
public abstract class CustomProperty<F, T> extends SimplePropertyExpression<F, T> {

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

    public abstract T converting(F original);

    @Override
    public @NotNull T convert(@NotNull F entity) {
        return converting(entity);
    }

    @Override
    public @NotNull Class<? extends T> getReturnType() {
        return (Class<T>) propertyClass;
    }
}
