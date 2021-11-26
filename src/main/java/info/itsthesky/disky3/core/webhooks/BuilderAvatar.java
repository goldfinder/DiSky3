package info.itsthesky.disky3.core.webhooks;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Webhook Builder Avatar")
@Description("The custom avatar URL which will be shown on Discord for this webhook builder.")
public class BuilderAvatar extends SimplePropertyExpression<WebhookMessageBuilder, String> {

    static {
        register(
                BuilderAvatar.class,
                String.class,
                "web[( |-)]hook avatar [ur(i|l)]",
                "webhookbuilder"
        );
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET)
            return new Class[] {String.class};
        return new Class[0];
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta == null || delta.length == 0 || delta[0] == null || mode != Changer.ChangeMode.SET)
            return;
        final WebhookMessageBuilder builder = getExpr().getSingle(e);
        if (builder == null)
            return;
        final String name = (String) delta[0];
        builder.setAvatarUrl(name);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "webhook avatar url";
    }

    @Override
    public @Nullable String convert(WebhookMessageBuilder builder) {
        return builder.build().getAvatarUrl();
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }
}
