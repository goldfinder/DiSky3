package info.itsthesky.disky3.core.webhooks;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Name("Make Webhook Send Message")
@Description({
        "Make a specific webhook client send a webhook builder.",
        "Every webhook were made from a channel and therefore the message will be sent here.",
        "DiSky will make a bridge between webhook & JDA librairies, so a bot could be needed to store the message sent."
})
@Examples("set {_b} to new webhook builder\n" +
        "set webhook avatar of {_b} to avatar of event-user\n" +
        "set webhook name of {_b} to \"Name\"\n" +
        "set webhook content of {_b} to \"Hello World\"\n" +
        "make {_client} send {_b} and store it in {_msg} with bot \"bot\"\n" +
        "reply with \"Sent! `%discord id of {_msg}%`\"")
public class MakeSent extends WaiterEffect<UpdatingMessage> {

    static {
        Skript.registerEffect(
                MakeSent.class,
                "make [the] [webhook] [client] %webhookclient% send [the] [builder] %webhookbuilder% [with [the] [embed] %-embedbuilders%] [and store (it|the message) in %-object% [(with|using) [the] [bot] %-bot%]]"
        );
    }

    private Expression<WebhookClient> exprClient;
    private Expression<WebhookMessageBuilder> exprBuilder;
    private Expression<EmbedBuilder> exprEmbeds;
    private Expression<Bot> exprBot;

    @Override
    @SuppressWarnings("unchecked")
    public boolean initEffect(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprClient = (Expression<WebhookClient>) expressions[0];
        exprBuilder = (Expression<WebhookMessageBuilder>) expressions[1];
        exprEmbeds = (Expression<EmbedBuilder>) expressions[2];
        setChangedVariable((Variable<UpdatingMessage>) expressions[3]);
        exprBot = (Expression<Bot>) expressions[4];
        return true;
    }

    @Override
    public void runEffect(Event e) {
        final WebhookClient client = exprClient.getSingle(e);
        WebhookMessageBuilder builder = exprBuilder.getSingle(e);
        final EmbedBuilder[] embeds = Utils.verifyVars(e, exprEmbeds, new EmbedBuilder[0]);
        if (client == null || builder == null) {
            restart();
            return;
        }

        if (embeds.length != 0)
            builder.addEmbeds(Arrays.stream(embeds).map(em -> WebhookEmbedBuilder.fromJDA(em.build()).build()).toArray(WebhookEmbed[]::new));

        client.send(builder.build()).thenAccept(msg -> {
            @Nullable Bot bot = Utils.verifyVar(e, exprBot);
            if (bot == null)
                bot = BotManager.getLoadedBots().get(0);

            bot
                    .getCore()
                    .getTextChannelById(msg.getChannelId())
                    .retrieveMessageById(msg.getId())
                    .queue(message -> restart(UpdatingMessage.from(message)));
        });
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "make client " + exprClient.toString(e, debug) + " send builder " + exprBuilder.toString(e, debug);
    }

}
