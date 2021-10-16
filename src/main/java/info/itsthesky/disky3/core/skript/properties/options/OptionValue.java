package info.itsthesky.disky3.core.skript.properties.options;

import info.itsthesky.disky3.api.skript.properties.ChoiceProperty;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.jetbrains.annotations.NotNull;

public class OptionValue extends ChoiceProperty<String> {

    static {
        register(
                OptionValue.class,
                String.class,
                "[discord] [option] value"
        );
    }

    @Override
    public String converting(SelectOption original) {
        return original.getValue();
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }
}
