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
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Create Webhook")
@Description({"Create a new webhook in a specific channel and with a name.",
"Store it to use it later, Discord has limitation with both webhook creation & edition.",
"Use a webhook builder to send custom messages from this client!"})
@Examples("set {_b} to new webhook builder\n" +
        "set webhook avatar of {_b} to avatar of event-user\n" +
        "set webhook name of {_b} to \"Name\"\n" +
        "set webhook content of {_b} to \"Hello World\"\n" +
        "make {_client} send {_b} and store it in {_msg} with bot \"bot\"\n" +
        "reply with \"Sent! `%discord id of {_msg}%`\"")
public class CreateWebhook extends WaiterEffect<WebhookClient> {

    static {
        Skript.registerEffect(
                CreateWebhook.class,
                "(create|make) [the] web[( |-)]hook client (with name|named) %string% (in|of|from) [the] [channel] %channel% [(with|using) [the] [bot] %-bot%] and store (it|the client) in %object%"
        );
    }

    private Expression<String> exprName;
    private Expression<GuildChannel> exprChannel;
    private Expression<Bot> exprBot;

    @Override
    public boolean initEffect(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprName = (Expression<String>) expressions[0];
        exprChannel = (Expression<GuildChannel>) expressions[1];
        exprBot = (Expression<Bot>) expressions[2];
        setChangedVariable((Variable<WebhookClient>) expressions[3]);
        return true;
    }

    @Override
    public void runEffect(Event e) {
        final String name = exprName.getSingle(e);
        GuildChannel guildChannel = exprChannel.getSingle(e);
        final @Nullable Bot bot = Utils.verifyVar(e, exprBot);
        if (name == null || guildChannel == null || !(guildChannel.getType().equals(ChannelType.TEXT)))
            return;

        final TextChannel channel;
        if (bot != null) {
            channel = bot.getCore().getTextChannelById(guildChannel.getId());
        } else {
            channel = (TextChannel) guildChannel;
        }

        channel
                .createWebhook(name)
                .queue(webhook -> {
                    restart(WebhookClient.withUrl(webhook.getUrl()));
                });
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "create new web hook client " + exprName.toString(e, debug) + " in channel " + exprChannel.toString(e, debug);
    }
}
