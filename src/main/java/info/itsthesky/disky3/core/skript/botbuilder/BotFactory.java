package info.itsthesky.disky3.core.skript.botbuilder;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.config.validate.SectionValidator;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.lang.util.SimpleLiteral;
import ch.njol.skript.util.StringMode;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.BotApplication;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.NodeInformation;
import info.itsthesky.disky3.core.skript.effects.EffManageBotBuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Class that will handle the main bot defining factory.
 */
public final class BotFactory {

    private static final String listPattern = "\\s*,\\s*|\\s+(and|or|, )\\s+";

    private static final SectionValidator validator = new SectionValidator()
            .addEntry("token", false)
            .addEntry("intents", true)
            .addSection("on ready", true)
            .addSection("application", true)
            .addSection("on guild ready", true);

    private static final SectionValidator appValidator = new SectionValidator()
            .addEntry("application id", false)
            .addEntry("application secret", false);

    public static @Nullable BotApplication buildApp(SectionNode node, String name) {
        if (node == null)
            return null;

        node.convertToEntries(0);
        if (!appValidator.validate(node))
            return null;

        final String appID = ScriptLoader.replaceOptions(node.get("application id", ""));
        final String appSecret = ScriptLoader.replaceOptions(node.get("application secret", ""));

        return new BotApplication(name, appID, appSecret);
    }

    public static @Nullable BotEntity build(SectionNode node, String name) {
        if (node == null)
            return null;
        
        node.convertToEntries(0);
        if (!validator.validate(node))
            return null;

        final Event event = new BotParsingEvent();

        final String token = parseExpression(ScriptLoader.replaceOptions(node.get("token", ""))).getSingle(event);
        if (Utils.isAnyNull(token)) {
            Skript.error("The token is require and cannot be null!");
            return null;
        }

        final String inputIntent = ScriptLoader.replaceOptions(node.get("intents", ""));
        final boolean defaultIntents = Utils.equalsAnyIgnoreCase(inputIntent, "all default intents", "default intents");

        final String[] unparsedIntents = inputIntent.split(listPattern);

        final List<GatewayIntent> intents;
        if (defaultIntents) {
            intents = Arrays.asList(EffManageBotBuilder.defaults);
        } else {
            intents = new ArrayList<>();
            for (String intent : unparsedIntents) {
                try {
                    intents.add(GatewayIntent.valueOf(intent.toUpperCase(Locale.ROOT).replaceAll(" ", "_")));
                } catch (Exception ex) {
                    Skript.error("Unknown intent: " + intent);
                    return null;
                }
            }
        }

        final List<TriggerItem> onReady = ScriptLoader.loadItems((SectionNode) node.get("on ready"));
        final List<TriggerItem> onGuildReady = node.get("on guild ready") == null ? new ArrayList<>() : ScriptLoader.loadItems((SectionNode) node.get("on guild ready"));

        final SectionNode appNode = (SectionNode) node.get("application");
        final @Nullable BotApplication app = buildApp(appNode, name);

        final BotEntity entity = new BotEntity(name, token, app, intents, onReady, onGuildReady);
        try {
            BotManager.add(entity.build());
        } catch (Exception ex) {
            DiSky.exception(ex, new NodeInformation(node));
            return null;
        }

        return entity;
    }

    private static Expression<String> parseExpression(String text) {
        if (text.startsWith("\"") && text.endsWith("\"")) {
            text = text.substring(1, text.length() - 1);
        }
        Expression<String> expr = VariableString.newInstance(text, StringMode.MESSAGE);
        try {
            if (((VariableString) expr).isSimple()) {
                expr = new SimpleLiteral<>(text, false);
            }
        } catch (NullPointerException ignored) { }
        return expr;
    }
}
