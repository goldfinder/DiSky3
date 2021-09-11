package info.itsthesky.disky3.core.skript.properties.message;

import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.PropertyCondition;
import org.jetbrains.annotations.NotNull;

public class MessageIsEdited extends PropertyCondition<UpdatingMessage> {

    static {
        register(
                MessageIsEdited.class,
                PropertyType.BE,
                "edited",
                "message"
        );
    }

    @Override
    public boolean check(@NotNull UpdatingMessage msg) {
        return msg.getMessage().isEdited();
    }

    @Override
    protected String getPropertyName() {
        return "edited";
    }
}
