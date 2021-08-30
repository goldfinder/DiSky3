package info.itsthesky.disky3.core.skript.properties.message;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.properties.MultipleMessageProperty;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

@Name("Message Mentioned Members")
@Description("Get every mentioned members in a message. If the message doesn't come from a guild it will return an empty array!")
@Since("3.0")
@Examples("mentioned members of event-message")
public class MessageMentionedMembers extends MultipleMessageProperty<Member> {

    static {
        register(
                MessageMentionedMembers.class,
                Member.class,
                "[discord] [message] mentioned members"
        );
    }

    @Override
    public Member[] converting(UpdatingMessage original) {
        if (original.getMessage().isFromGuild())
            return original.getMessage().getMentionedMembers().toArray(new Member[0]);
        return new Member[0];
    }

}
