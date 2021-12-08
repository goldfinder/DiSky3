package info.itsthesky.disky3.core.oauth;

import bell.oauth.discord.main.OAuthBuilder;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public class OAuthDomain extends SimplePropertyExpression<OAuthWrapper, String> {

    static {
        register(
                OAuthDomain.class,
                String.class,
                "[redirect] domain[s]",
                "oauthmanager"
        );
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET)
            return new Class[] {String.class};
        return new Class[0];
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        final OAuthWrapper builder = getExpr().getSingle(e);
        if (builder == null || delta == null || delta.length == 0 || delta[0] == null)
            return;
        final String domain = (String) delta[0];
        final String ip = "http://" + domain + ":"+OAuthManager.getPort()+"/oauth/";
        builder.setRedirectURI(ip);
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "oauth domain";
    }

    @Override
    public String convert(OAuthWrapper t) {
        return t.getDomain();
    }
}
