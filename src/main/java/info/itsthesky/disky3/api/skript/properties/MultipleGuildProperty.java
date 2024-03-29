package info.itsthesky.disky3.api.skript.properties;

import info.itsthesky.disky3.api.skript.properties.base.MultipleCustomProperty;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

/**
 * @author ItsTheSky
 */
public abstract class MultipleGuildProperty<T> extends MultipleCustomProperty<Guild, T> {

    @Override
    protected @NotNull String getPropertyName() {
        return "guild";
    }

    static {
        setFromType("guild");
    }
}
