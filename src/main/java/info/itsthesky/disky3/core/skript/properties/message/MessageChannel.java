package info.itsthesky.disky3.core.skript.properties.message;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.properties.MessageProperty;
import net.dv8tion.jda.api.entities.TextChannel;

@Name("Message Text Channel")
@Description("Get the text channel were the message was sent. Can be null if it's in PM or not in guild!")
@Since("3.0")
@Examples("channel of event-message")
public class MessageChannel extends MessageProperty<TextChannel> {

    static {
        register(
                MessageChannel.class,
                TextChannel.class,
                "[discord] [message] [text]( |-)channel"
        );
    }

    @Override
    public TextChannel converting(UpdatingMessage original) {
        if (original.getMessage().isFromGuild())
            return original.getMessage().getTextChannel();
        return null;
    }

}
