package info.itsthesky.disky3.core.skript.properties.bots;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@Name("Bot Intents")
@Description({"Get every enabled intents of the specified bot.",
        "You cannot change this property, intents can only be changed by re-loading the bot!"})
@Examples({"gateway intents of event-bot",
        "enabled intents of bot \"name\""})
public class BotIntents extends MultiplyPropertyExpression<Bot, GatewayIntent> {

    static {
        register(
                BotIntents.class,
                GatewayIntent.class,
                "[all] [enabled] [gateway] intent[s]",
                "bot"
        );
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        return new Class[0];
    }

    @Override
    public @NotNull Class<? extends GatewayIntent> getReturnType() {
        return GatewayIntent.class;
    }

    @Override
    protected String getPropertyName() {
        return "gateway intents";
    }

    @Override
    protected GatewayIntent[] convert(Bot t) {
        return t.getCore().getGatewayIntents().toArray(new GatewayIntent[0]);
    }
}
