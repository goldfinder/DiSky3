package info.itsthesky.disky3.core.skript;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.events.bukkit.SkriptStartEvent;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.AsyncEffect;
import info.itsthesky.disky3.api.skript.EffectSection;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

@Name("Login to Bot")
@Description("Register and load a new discord bot from a token and with a specific Name." +
        "\nYou need to follow Discord's developer instruction in order to generate a new bot with a token")
@Examples("login to \"TOKEN\" with name \"MyBot\"")
@Since("1.0")
public class EffRegisterBot extends AsyncEffect {

    static {
        Skript.registerEffect(EffRegisterBot.class,
                "login to [token] %string% with [the] (name|id) %string%");
    }

    private Expression<String> exprName;
    private Expression<String> exprToken;
    private boolean scope;
    private NodeInformation info;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (ScriptLoader.isCurrentEvent(SkriptStartEvent.class)) {
            Skript.error("We don't recommend using the login effect in a " + ScriptLoader.getCurrentEventName() + " event. Use 'on load:' instead!", ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        info = new NodeInformation();

        scope = EffectSection.isCurrentSection(ScopeBotBuilder.class);
        if (!scope)
            DiSky.warn("WARNING! You are login in to a new bot WITHOUT managing any intents! " + info.getDebugLabel());
        exprToken = (Expression<String>) exprs[0];
        exprName = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String name = exprName.getSingle(e);
        String token = exprToken.getSingle(e);
        if (name == null || token == null) return;
        final Bot bot;
        if (!BotManager.isLoaded(name)) {
            try {
                bot = new Bot(
                        (scope ? ScopeBotBuilder.lastBuilder.setToken(token) : JDABuilder.createDefault(token)).build(),
                        name
                );
            } catch (Exception ex) {
                DiSky.exception(ex, info);
                return;
            }
            BotManager.add(bot);
            DiSky.success("The bot named " + name + " has been loaded! ("+bot.getCore().getGatewayIntents().size()+" intents enabled)");
        } else {
            DiSky.exception(new DiSkyException("The bot called " + name + " is already loaded!"), info);
        }
    }

    @Override
    public @NotNull String toString(Event e, boolean debug) {
        return "register new discord bot from token " + exprToken.toString(e, debug) + " with name " + exprName.toString(e, debug);
    }

}