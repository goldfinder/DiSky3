package info.itsthesky.disky3.core.skript.properties.options;

import info.itsthesky.disky3.api.skript.properties.ChoiceProperty;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.jetbrains.annotations.NotNull;

public class OptionDescription extends ChoiceProperty<String> {

    static {
        register(
                OptionDescription.class,
                String.class,
                "[discord] [option] desc[ription]"
        );
    }

    @Override
    public String converting(SelectOption original) {
        return original.getDescription();
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }
}
