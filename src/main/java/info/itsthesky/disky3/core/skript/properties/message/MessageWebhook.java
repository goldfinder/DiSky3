package info.itsthesky.disky3.core.skript.properties.message;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.PropertyCondition;
import org.jetbrains.annotations.NotNull;

@Name("Is Webhook Message")
@Description({"Check if the specified message come from a webhook or not."})
@Examples({"if event-message is a webhook message:",
        "if event-message is web hook:"})
public class MessageWebhook extends PropertyCondition<UpdatingMessage> {

    static {
        register(
                MessageWebhook.class,
                PropertyType.BE,
                "[a] web( |-)hook [message]",
                "message"
        );
    }

    @Override
    public boolean check(@NotNull UpdatingMessage message) {
        return message.getMessage().isWebhookMessage();
    }

    @Override
    protected String getPropertyName() {
        return "webhook";
    }
}
