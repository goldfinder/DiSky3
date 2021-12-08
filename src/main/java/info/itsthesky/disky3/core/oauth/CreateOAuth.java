package info.itsthesky.disky3.core.oauth;

import bell.oauth.discord.main.OAuthBuilder;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.bot.Bot;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("New OAuth2 System")
@Description({"Initialize a new OAuth2 system fo rDiSky and the specified bot.",
        "You'll then need to input scopes and an optional domain to redirect the user.",
        "**OAuth2 is a pretty complex system and is not good for newcomers!**"})
public class CreateOAuth extends SimpleExpression<OAuthWrapper> {

    static {
        Skript.registerExpression(
                CreateOAuth.class,
                OAuthWrapper.class,
                ExpressionType.COMBINED,
                "(create|init|start|make) [a] new [o]auth[(orization|2)] [system] (with|using) [the] [bot] %bot%"
        );
    }

    private Expression<Bot> exprBot;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprBot = (Expression<Bot>) exprs[0];
        return true;
    }

    @Override
    protected OAuthWrapper @NotNull [] get(@NotNull Event e) {
        final Bot bot = exprBot.getSingle(e);
        if (!OAuthManager.checkBot(bot))
            return new OAuthWrapper[0];

        return new OAuthWrapper[] {new OAuthWrapper(bot, new OAuthBuilder(bot.getApplication().getClientID(), bot.getApplication().getClientSecret()))};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends OAuthWrapper> getReturnType() {
        return OAuthWrapper.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "create new oauth2 system with bot " + exprBot.toString(e, debug);
    }
}
