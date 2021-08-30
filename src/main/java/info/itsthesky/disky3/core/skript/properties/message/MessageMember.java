package info.itsthesky.disky3.core.skript.properties.message;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.properties.MessageProperty;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

@Name("Message Member Author")
@Description("Get the member instance of the message's author. Can be null if it's in PM or not in guild!")
@Since("3.0")
@Examples("member writer of event-message")
public class MessageMember extends MessageProperty<Member> {

    static {
        register(
                MessageMember.class,
                User.class,
                "[discord] [message] member (author|writer)"
        );
    }

    @Override
    public Member converting(UpdatingMessage original) {
        if (original.getMessage().isFromGuild())
            return original.getMessage().getMember();
        return null;
    }

}
