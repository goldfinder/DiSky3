package info.itsthesky.disky3.core.skript.properties.emotes;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import info.itsthesky.disky3.api.emojis.Emote;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Emote Image URL")
@Description({"Get the URL of this emote.",
        "Only emote have image URL, emoji are from Discord and will therefore return none here."
})
@Examples({"emote url of event-emote",
        "emote image of reaction \"disky\" # Custom emoji only"})
public class EmoteURL extends SimplePropertyExpression<Emote, String> {

    static {
        register(
                EmoteURL.class,
                String.class,
                "[the] emo(te|ji) (ur(i|l)|image [url])",
                "emote"
        );
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "emote image url";
    }

    @Override
    public @Nullable String convert(Emote emote) {
        return emote.isEmote() ? emote.getEmote().getImageUrl() : null;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

}
