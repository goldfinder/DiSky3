package info.itsthesky.disky3.core.skript.properties.message;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.properties.MessageProperty;
import net.dv8tion.jda.api.entities.User;

@Name("Message Jump URL")
@Description("Get the jump URL of the specific message.")
@Since("3.0")
@Examples("jump url of event-message")
public class MessageJumpURL extends MessageProperty<String> {

    static {
        register(
                MessageJumpURL.class,
                String.class,
                "[discord] [message] [jump] url"
        );
    }

    @Override
    public String converting(UpdatingMessage original) {
        return original.getMessage().getJumpUrl();
    }

}
