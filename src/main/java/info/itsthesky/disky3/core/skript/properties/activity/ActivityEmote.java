package info.itsthesky.disky3.core.skript.properties.activity;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import info.itsthesky.disky3.api.emojis.Emote;
import net.dv8tion.jda.api.entities.Activity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ActivityEmote extends SimplePropertyExpression<Activity, Emote> {

    static {
        register(
                ActivityEmote.class,
                Emote.class,
                "activity emo(ji|te)",
                "activity"
        );
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "activity emote";
    }

    @Override
    public @Nullable Emote convert(@NotNull Activity activity) {
        return activity.getEmoji() == null ? null : Emote.fromActivityEmoji(activity.getEmoji());
    }

    @Override
    public @NotNull Class<? extends Emote> getReturnType() {
        return Emote.class;
    }
}
