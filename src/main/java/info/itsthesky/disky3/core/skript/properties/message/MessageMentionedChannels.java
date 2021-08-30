package info.itsthesky.disky3.core.skript.properties.message;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.properties.MultipleMessageProperty;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

@Name("Message Text Channels")
@Description("Get every mentioned text channels in a message.")
@Since("3.0")
@Examples("mentioned channels of event-message")
public class MessageMentionedChannels extends MultipleMessageProperty<TextChannel> {

    static {
        register(
                MessageMentionedChannels.class,
                TextChannel.class,
                "[discord] [message] mentioned [text] channels"
        );
    }

    @Override
    public TextChannel[] converting(UpdatingMessage original) {
        if (original.getMessage().isFromGuild())
            return original.getMessage().getMentionedChannels().toArray(new TextChannel[0]);
        return new TextChannel[0];
    }

}
