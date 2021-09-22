package info.itsthesky.disky3.core.skript.properties.message;

import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.PropertyCondition;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

public class MessageIsPosted extends PropertyCondition<UpdatingMessage> {

    static {
        register(
                MessageIsPosted.class,
                PropertyType.BE,
                "(publish|post|crosspost)ed",
                "message"
        );
    }

    @Override
    public boolean check(@NotNull UpdatingMessage msg) {
        return msg.getMessage().getFlags().contains(Message.MessageFlag.CROSSPOSTED);
    }

    @Override
    protected String getPropertyName() {
        return "published";
    }
}
