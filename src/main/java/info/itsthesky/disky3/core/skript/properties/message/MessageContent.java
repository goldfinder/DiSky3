package info.itsthesky.disky3.core.skript.properties.message;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.properties.MessageProperty;

@Name("Message Content")
@Description("Get the raw (non formatted) content of a sent message.")
@Since("3.0")
@Examples("content of event-message")
public class MessageContent extends MessageProperty<String> {

    static {
        register(
                MessageContent.class,
                String.class,
                "[discord] [message] content"
        );
    }

    @Override
    public String converting(UpdatingMessage original) {
        return original.getMessage().getContentRaw();
    }

}
