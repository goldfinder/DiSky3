package info.itsthesky.disky3.core.skript.properties.message;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.properties.MultipleMessageProperty;
import net.dv8tion.jda.api.entities.Role;

@Name("Message Mentioned Roles")
@Description("Get every mentioned Roles in a message. If the message doesn't come from a guild it will return an empty array!")
@Since("3.0")
@Examples("mentioned roles of event-message")
public class MessageMentionedRoles extends MultipleMessageProperty<Role> {

    static {
        register(
                MessageMentionedRoles.class,
                Role.class,
                "[discord] [message] mentioned roles"
        );
    }

    @Override
    public Role[] converting(UpdatingMessage original) {
        if (original.getMessage().isFromGuild())
            return original.getMessage().getMentionedRoles().toArray(new Role[0]);
        return new Role[0];
    }

}
