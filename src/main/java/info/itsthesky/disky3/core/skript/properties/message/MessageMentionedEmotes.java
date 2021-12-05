package info.itsthesky.disky3.core.skript.properties.message;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.properties.MultipleMessageProperty;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.TextChannel;

@Name("Message Emotes")
@Description({"Get every mentioned emotes in a message.",
"This will only return custom emote, and will therefore not include discord emotes."})
@Since("3.0")
@Examples("mentioned emotes of event-message")
public class MessageMentionedEmotes extends MultipleMessageProperty<Emote> {

    static {
        register(
                MessageMentionedEmotes.class,
                Emote.class,
                "[discord] [message] mentioned emote[s]"
        );
    }

    @Override
    public Emote[] converting(UpdatingMessage original) {
        if (original.getMessage().isFromGuild())
            return original.getMessage().getEmotes().toArray(new Emote[0]);
        return new Emote[0];
    }

}
