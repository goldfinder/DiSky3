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
import club.minnced.discord.webhook.WebhookClient;
import info.itsthesky.disky3.api.Utils;
import net.dv8tion.jda.api.entities.ThreadChannel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Get Webhook Client")
@Description({"Get a webhook client from either its URL, or its ID + Token.",
"This client will be the base before directly send message with it.",
"You can specify a thread to make the client talk only there, else it will talk in the text channel attached."})
@Examples({"set {_client} to webhook with url \"XXX\"",
"set {_client} to webhook with id \"000\" and token \"XXX\""})
public class GetWebhook extends SimpleExpression<WebhookClient> {

    static {
        Skript.registerExpression(
                GetWebhook.class,
                WebhookClient.class,
                ExpressionType.COMBINED,
                "[get] [the] web[( |-)]hook [client] (with|from) [the] ur(l|i) %string% [in [the] [thread] %-thread%]",
                "[get] [the] web[( |-)]hook [client] (with|from) [the] id %string% [and] [(with|from)] [the] token %string% [in [the] [thread] %-thread%]"
        );
    }

    private boolean isFromURL;
    private Expression<String> exprIdOrURL;
    private Expression<String> exprToken;
    private Expression<ThreadChannel> exprThread;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        isFromURL = matchedPattern == 0;
        exprIdOrURL = (Expression<String>) exprs[0];
        if (!isFromURL) {
            exprToken = (Expression<String>) exprs[1];
            exprThread = (Expression<ThreadChannel>) exprs[2];
        } else {
            exprThread = (Expression<ThreadChannel>) exprs[1];
        }
        return true;
    }

    @Override
    protected WebhookClient @NotNull [] get(@NotNull Event e) {
        final String idOrUrl = exprIdOrURL.getSingle(e);
        final @Nullable String token = Utils.verifyVar(e, exprToken);
        final @Nullable ThreadChannel thread = Utils.verifyVar(e, exprThread);
        if (idOrUrl == null || (!isFromURL && token == null))
            return new WebhookClient[0];
        if (!isFromURL && !Utils.isNumeric(idOrUrl))
            return new WebhookClient[0];
        final WebhookClient client;
        if (isFromURL) {
            client = WebhookClient.withUrl(idOrUrl);
        } else {
            client = WebhookClient.withId(Long.parseLong(idOrUrl), token);
        }
        return new WebhookClient[] {thread != null ? client.onThread(thread.getIdLong()) : client};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends WebhookClient> getReturnType() {
        return WebhookClient.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "webhook with " + (isFromURL ? "url " + exprIdOrURL.toString(e, debug) : " id " + exprIdOrURL.toString(e, debug) + " and token " + exprToken.toString(e, debug));
    }
}
