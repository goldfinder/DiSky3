package info.itsthesky.disky3.core.skript.properties.options;

import info.itsthesky.disky3.api.emojis.Emote;
import info.itsthesky.disky3.api.skript.properties.ChoiceProperty;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.jetbrains.annotations.NotNull;

public class OptionEmote extends ChoiceProperty<Emote> {

    static {
        register(
                OptionEmote.class,
                String.class,
                "[discord] [option] emo(te|ji)"
        );
    }

    @Override
    public Emote converting(SelectOption original) {
        return new Emote(original.getEmoji());
    }

    @Override
    public @NotNull Class<? extends Emote> getReturnType() {
        return Emote.class;
    }
}
