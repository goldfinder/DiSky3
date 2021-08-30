package info.itsthesky.disky3.core.skript.properties.message;

import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.properties.MessageProperty;

public class MessageReferenced extends MessageProperty<UpdatingMessage> {

    static {
        register(
                MessageReferenced.class,
                UpdatingMessage.class,
                "[discord] [message] referenc(ing|ed) message"
        );
    }

    @Override
    public UpdatingMessage converting(UpdatingMessage original) {
        if (original.getMessage().getReferencedMessage() == null) return null;
        return UpdatingMessage.from(original.getMessage().getReferencedMessage());
    }

}
