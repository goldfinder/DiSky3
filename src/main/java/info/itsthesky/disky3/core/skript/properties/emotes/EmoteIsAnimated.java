package info.itsthesky.disky3.core.skript.properties.emotes;

import info.itsthesky.disky3.api.emojis.Emote;
import info.itsthesky.disky3.api.skript.PropertyCondition;
import org.jetbrains.annotations.NotNull;

public class EmoteIsAnimated extends PropertyCondition<Emote> {

    static {
        register(
                EmoteIsAnimated.class,
                PropertyType.BE,
                "animated",
                "emote"
        );
    }

    @Override
    public boolean check(@NotNull Emote emote) {
        return isNegated() != emote.isAnimated();
    }

    @Override
    protected String getPropertyName() {
        return "animated";
    }

}
