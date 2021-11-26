package info.itsthesky.disky3.core.webhooks;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("New Webhook Builder")
@Description({"Create a new DiSky-side webhook builder, where you'll be able to change name & avatar."})
@Examples({"set {_b} to new webhook builder",
        "set webhook avatar of {_b} to \"URL\"",
        "set webhook name of {_b} to \"Name\"",
        "set webhook content of {_b} to \"Hello World\"",
        "make {_client} send builder {_b}"})
public class NewBuilder extends SimpleExpression<WebhookMessageBuilder> {

    static {
        Skript.registerExpression(
                NewBuilder.class,
                WebhookMessageBuilder.class,
                ExpressionType.COMBINED,
                "[(create|make)] [a] new web[( |-)]hook builder"
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    protected WebhookMessageBuilder @NotNull [] get(@NotNull Event e) {
        return new WebhookMessageBuilder[] {new WebhookMessageBuilder()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends WebhookMessageBuilder> getReturnType() {
        return WebhookMessageBuilder.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "new webhook builder";
    }
}

