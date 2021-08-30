package info.itsthesky.disky3.core.skript.properties.message;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.properties.MessageProperty;
import net.dv8tion.jda.api.entities.User;

@Name("Message User Author")
@Description("Get the user instance of the message's author. Can be null in case of the message was sent by a webhook.")
@Since("3.0")
@Examples("author of event-message")
public class MessageAuthor extends MessageProperty<User> {

    static {
        register(
                MessageAuthor.class,
                User.class,
                "[discord] [message] (user|author|writer)"
        );
    }

    @Override
    public User converting(UpdatingMessage original) {
        return original.getMessage().getAuthor();
    }

}
