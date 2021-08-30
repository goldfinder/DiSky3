package info.itsthesky.disky3.core.skript.properties.message;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.properties.MultipleMessageProperty;
import net.dv8tion.jda.api.entities.MessageSticker;
import net.dv8tion.jda.api.entities.TextChannel;

@Name("Message Text Channels")
@Description("Get every mentioned text channels in a message.")
@Since("3.0")
@Examples("mentioned channels of event-message")
public class MessageStickers extends MultipleMessageProperty<MessageSticker> {

    static {
        register(
                MessageStickers.class,
                MessageSticker.class,
                "[discord] [message] mentioned [text] channels"
        );
    }

    @Override
    public MessageSticker[] converting(UpdatingMessage original) {
        if (original.getMessage().isFromGuild())
            return original.getMessage().getStickers().toArray(new MessageSticker[0]);
        return new MessageSticker[0];
    }

}
