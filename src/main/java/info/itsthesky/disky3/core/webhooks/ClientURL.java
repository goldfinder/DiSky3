package info.itsthesky.disky3.core.webhooks;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import club.minnced.discord.webhook.WebhookClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Webhook Client URL")
@Description({"Get the private & confidential webhook URL of a client.",
"Anyone who have this will be able to send messages at any moment from any where!"})
@Examples("set {_url} to client url of {_client}")
public class ClientURL extends SimplePropertyExpression<WebhookClient, String> {

    static {
        register(
                ClientURL.class,
                String.class,
                "(client|webhook) ur(l|i)",
                "webhookclient"
        );
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "webhook url";
    }

    @Override
    public @Nullable String convert(WebhookClient client) {
        return client.getUrl();
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }
}
