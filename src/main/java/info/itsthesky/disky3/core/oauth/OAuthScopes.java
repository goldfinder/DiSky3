package info.itsthesky.disky3.core.oauth;

import bell.oauth.discord.main.OAuthBuilder;
import ch.njol.skript.classes.Changer;
import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class OAuthScopes extends MultiplyPropertyExpression<OAuthWrapper, String> {

    static {
        register(
                OAuthScopes.class,
                String.class,
                "[all] [enabled] scope[s]",
                "oauthmanager"
        );
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET)
            return new Class[] {String[].class};
        return new Class[0];
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        final OAuthWrapper builder = getExpr().getSingle(e);
        if (builder == null || delta == null || delta.length == 0)
            return;
        final String[] scopes = Arrays.stream(delta).map(Object::toString).toArray(String[]::new);
        builder.setScopes(scopes);
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "oauth scopes";
    }

    @Override
    protected String[] convert(OAuthWrapper t) {
        return t.getScopes();
    }
}
