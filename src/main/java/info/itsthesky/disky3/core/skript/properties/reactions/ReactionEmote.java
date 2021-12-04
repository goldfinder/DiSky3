package info.itsthesky.disky3.core.skript.properties.reactions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import info.itsthesky.disky3.api.emojis.Emote;
import net.dv8tion.jda.api.entities.MessageReaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Reaction Emote")
@Description({"Get the emote or emoji that represent a reaction.",
        "Emote (or emoji) are actual emote that could be used in messages;",
        "Reactions are what user can to with message using emote, aka they 'react' to a message, and therefore a reaction is created."})
public class ReactionEmote extends SimplePropertyExpression<MessageReaction, Emote> {

    static {
        register(
                ReactionEmote.class,
                Emote.class,
                "[the] react[ion] emo(te|ji)",
                "reaction"
        );
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "emote";
    }

    @Override
    public @Nullable Emote convert(MessageReaction reaction) {
        return Emote.fromReaction(reaction.getReactionEmote());
    }

    @Override
    public @NotNull Class<? extends Emote> getReturnType() {
        return Emote.class;
    }
}
