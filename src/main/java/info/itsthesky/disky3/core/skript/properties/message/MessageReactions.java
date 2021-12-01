package info.itsthesky.disky3.core.skript.properties.message;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.emojis.Emote;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.properties.MultipleMessageProperty;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageReaction;

import java.util.stream.Collectors;

@Name("Message Reactions")
@Description({"Get every reactions of a message.",
"Because of Discord's limitation, we cannot get which user reacted with which reaction, you'll have to count thet yourself."})
@Examples("reactions of event-message")
public class MessageReactions extends MultipleMessageProperty<Emote> {

    static {
        register(
                MessageReactions.class,
                Emote.class,
                "[discord] [message] (emo(te|ji)|reaction)[s]"
        );
    }

    @Override
    public Emote[] converting(UpdatingMessage original) {
        return original
                .getMessage()
                .getReactions()
                .stream()
                .map(MessageReaction::getReactionEmote)
                .map(em -> Emote.fromJDA(em.getEmote()))
                .toArray(Emote[]::new);
    }
}
