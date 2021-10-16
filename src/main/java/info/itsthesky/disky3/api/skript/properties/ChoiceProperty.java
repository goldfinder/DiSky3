package info.itsthesky.disky3.api.skript.properties;

import info.itsthesky.disky3.api.skript.properties.base.CustomProperty;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.jetbrains.annotations.NotNull;

/**
 * @author ItsTheSky
 */
public abstract class ChoiceProperty<T> extends CustomProperty<SelectOption, T> {

    @Override
    protected @NotNull String getPropertyName() {
        return "choice";
    }

    static {
        setFromType("guild");
    }
}
