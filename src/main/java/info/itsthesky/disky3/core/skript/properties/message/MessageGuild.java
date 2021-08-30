package info.itsthesky.disky3.core.skript.properties.message;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.properties.MessageProperty;
import net.dv8tion.jda.api.entities.Guild;

@Name("Message Guild")
@Description("Get the guild where the message was sent. Can be null if it's in PM or not in guild!")
@Since("3.0")
@Examples("guild of event-message")
public class MessageGuild extends MessageProperty<Guild> {

    static {
        register(
                MessageGuild.class,
                Guild.class,
                "[discord] [message] guild"
        );
    }

    @Override
    public Guild converting(UpdatingMessage original) {
        if (original.getMessage().isFromGuild())
            return original.getMessage().getGuild();
        return null;
    }

}
