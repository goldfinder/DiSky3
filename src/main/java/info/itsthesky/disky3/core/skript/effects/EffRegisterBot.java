package info.itsthesky.disky3.core.skript.effects;

import info.itsthesky.disky3.api.skript.WaiterEffect;
import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
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
import info.itsthesky.disky3.api.section.EffectSection;
import info.itsthesky.disky3.api.skript.NodeInformation;
import info.itsthesky.disky3.core.skript.ScopeBotBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

@Name("Login to Bot")
@Description("Register and load a new discord bot from a token and with a specific Name." +
        "\nYou need to follow Discord's developer instruction in order to generate a new bot with a token")
@Examples("login to \"TOKEN\" with name \"MyBot\"")
@Since("1.0")
public class EffRegisterBot extends WaiterEffect {

    private static final HashMap<String, QueueInfo> QUEUE_EFFECTS = new HashMap<>();

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
    public boolean initEffect(Expression@NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
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
    public void runEffect(@NotNull Event e) {
        String name = exprName.getSingle(e);
        String token = exprToken.getSingle(e);
        if (name == null || token == null) {
            restart();
            return;
        }
        if (!BotManager.isLoaded(name)) {
            try {
                final JDA jda = (scope ? ScopeBotBuilder.lastBuilder.setToken(token) : JDABuilder.createDefault(token)).build();
                jda.addEventListener(new BotLoadingListener());
                QUEUE_EFFECTS.put(jda.getSelfUser().getId(), new QueueInfo(jda, name, this));
            } catch (Exception ex) {
                DiSky.exception(ex, info);
                restart();
            }
        } else {
            DiSky.exception(new DiSkyException("The bot called " + name + " is already loaded!"), info);
            restart();
        }
    }

    public static class BotLoadingListener extends ListenerAdapter {

        @Override
        public void onReady(@NotNull ReadyEvent event) {
            final @Nullable QueueInfo info = QUEUE_EFFECTS.getOrDefault(event.getJDA().getSelfUser().getId(), null);
            if (info == null)
                return;
            final Bot bot = new Bot(info.getJda(), info.getName(), null);
            BotManager.add(bot);
            DiSky.success("The bot named " + info.getName() + " has been loaded! ("+bot.getCore().getGatewayIntents().size()+" intents enabled)");
            info.getEffect().restart();
        }
    }

    public static class QueueInfo {

        private JDA jda;
        private String name;
        private EffRegisterBot effect;

        public QueueInfo(JDA jda, String name, EffRegisterBot effect) {
            this.jda = jda;
            this.name = name;
            this.effect = effect;
        }

        public JDA getJda() {
            return jda;
        }

        public void setJda(JDA jda) {
            this.jda = jda;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public EffRegisterBot getEffect() {
            return effect;
        }

        public void setEffect(EffRegisterBot effect) {
            this.effect = effect;
        }
    }

    @Override
    public @NotNull String toString(Event e, boolean debug) {
        return "register new discord bot from token " + exprToken.toString(e, debug) + " with name " + exprName.toString(e, debug);
    }

}