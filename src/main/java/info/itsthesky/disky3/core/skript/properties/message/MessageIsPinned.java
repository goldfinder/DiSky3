package info.itsthesky.disky3.core.skript.properties.message;

import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.PropertyCondition;
import org.jetbrains.annotations.NotNull;

public class MessageIsPinned extends PropertyCondition<UpdatingMessage> {

    static {
        register(
                MessageIsPinned.class,
                PropertyType.BE,
                "pin[ned]",
                "message"
        );
    }

    @Override
    public boolean check(@NotNull UpdatingMessage msg) {
        return msg.getMessage().isPinned();
    }

    @Override
    protected String getPropertyName() {
        return "pinned";
    }
}
