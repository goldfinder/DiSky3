package info.itsthesky.disky3.core.skript.properties.region;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import info.itsthesky.disky3.api.emojis.Emote;
import info.itsthesky.disky3.api.emojis.updated.Emoji;
import info.itsthesky.disky3.api.emojis.updated.Emojis;
import net.dv8tion.jda.api.Region;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Region Emoji")
@Description({"Return an Emote object that this region should have (its flag).",
        "It should never be null, but can be if the JDA is not updated according to Discord."})
public class RegionFlag extends SimplePropertyExpression<Region, Emote> {

    static {
        register(
                RegionFlag.class,
                Emote.class,
                "region emo(te|ji)",
                "voiceregion"
        );
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "emoji";
    }

    @Override
    public @Nullable Emote convert(@NotNull Region region) {
        final Emoji emoji = Emojis.ofUnicode(region.getEmoji());
        if (emoji == null) {
            Skript.warning("This should never happen, but the region emoji is null!");
            return null;
        }
        return new Emote(emoji.shortcodes().get(0), emoji.unicode());
    }

    @Override
    public @NotNull Class<? extends Emote> getReturnType() {
        return Emote.class;
    }
}
