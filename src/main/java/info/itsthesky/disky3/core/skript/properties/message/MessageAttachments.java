package info.itsthesky.disky3.core.skript.properties.message;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.properties.MultipleMessageProperty;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

@Name("Message Attachment")
@Description("Get every attachment as custom object of a message")
@Since("3.0")
@Examples("attachments of event-message")
public class MessageAttachments extends MultipleMessageProperty<Message.Attachment> {

    static {
        register(
                MessageAttachments.class,
                Message.Attachment.class,
                "[discord] [message] attachment[s]"
        );
    }

    @Override
    public Message.Attachment[] converting(UpdatingMessage original) {
        return original.getMessage().getAttachments().toArray(new Message.Attachment[0]);
    }

}
