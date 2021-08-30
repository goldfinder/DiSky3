package info.itsthesky.disky3.core.skript.properties.message;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.properties.MultipleMessageProperty;
import net.dv8tion.jda.api.entities.User;

@Name("Message Mentioned Users")
@Description("Get every mentioned users in a message.")
@Since("3.0")
@Examples("mentioned users of event-message")
public class MessageMentionedUsers extends MultipleMessageProperty<User> {

    static {
        register(
                MessageMentionedUsers.class,
                User.class,
                "[discord] [message] mentioned users"
        );
    }

    @Override
    public User[] converting(UpdatingMessage original) {
        if (original.getMessage().isFromGuild())
            return original.getMessage().getMentionedUsers().toArray(new User[0]);
        return new User[0];
    }

}
