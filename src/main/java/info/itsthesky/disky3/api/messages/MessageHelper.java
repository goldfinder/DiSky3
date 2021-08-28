package info.itsthesky.disky3.api.messages;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public final class MessageHelper {

    public static MessageAction parseEditMessage(Message input, Object edit) {

        if (edit instanceof String) {
            return input.editMessage(edit.toString());
        } else if (edit instanceof EmbedBuilder) {
            return input.editMessageEmbeds(((EmbedBuilder) edit).build());
        } else if (edit instanceof MessageBuilder) {
            return input.editMessage(((MessageBuilder) edit).build());
        }
        return null;

    }

}
