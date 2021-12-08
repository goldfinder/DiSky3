package info.itsthesky.disky3.core.oauth;

import bell.oauth.discord.main.OAuthBuilder;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Built OAuth2 System")
@Description({"Get the final authorization URL that the user will have to accept once you changed domains & scopes.",
"If no scopes were provided, Discord will show an error while going to the returned link!"})
@Examples("reply with \"Click here to share your info: %built oauth {_oauth}%\"")
public class BuildOAuthURL extends SimpleExpression<String> {

    static {
        Skript.registerExpression(
                BuildOAuthURL.class,
                String.class,
                ExpressionType.COMBINED,
                "buil(d|t) [o]auth[2] [system] %oauthmanager% [(with|using) [the] state %-string%]"
        );
    }

    private Expression<OAuthWrapper> exprOAuth;
    private Expression<String> exprState;

    @Override
    protected String @NotNull [] get(@NotNull Event e) {
        final OAuthWrapper builder = exprOAuth.getSingle(e);
        final @Nullable String state = Utils.verifyVar(e, exprState);
        if (builder == null)
            return new String[0];
        return new String[] {builder.getInstance().getAuthorizationUrl(state)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "built oauth system " + exprOAuth.toString(e, debug);
    }

    @Override
    @SuppressWarnings("uncheckedoa")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprOAuth = (Expression<OAuthWrapper>) exprs[0];
        exprState = (Expression<String>) exprs[1];
        return true;
    }
}
