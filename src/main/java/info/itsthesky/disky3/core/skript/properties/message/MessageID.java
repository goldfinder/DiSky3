package info.itsthesky.disky3.core.skript.properties.message;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.properties.MessageProperty;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

@Name("Message ID")
@Description("Get the unique ID of the message.")
@Since("3.0")
@Examples("discord id of event-message")
public class MessageID extends MessageProperty<String> {

    static {
        register(
                MessageID.class,
                String.class,
                "discord [message] id"
        );
    }

    @Override
    public String converting(UpdatingMessage original) {
        return original.getID();
    }

}
