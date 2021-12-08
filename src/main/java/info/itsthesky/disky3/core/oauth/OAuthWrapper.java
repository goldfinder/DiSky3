package info.itsthesky.disky3.core.oauth;

import bell.oauth.discord.main.OAuthBuilder;
import info.itsthesky.disky3.api.ReflectionUtils;
import info.itsthesky.disky3.api.bot.Bot;
import net.dv8tion.jda.api.entities.ISnowflake;
import org.jetbrains.annotations.NotNull;

public class OAuthWrapper implements ISnowflake {

    private final OAuthBuilder instance;
    private final Bot bot;
    private String[] scopes;
    private String domain;

    public OAuthWrapper(Bot bot, OAuthBuilder instance) {
        this.instance = instance;
        this.bot = bot;
    }

    public OAuthBuilder getInstance() {
        return instance;
    }

    public String getSecret() {
        return ReflectionUtils.getFieldValue(OAuthBuilder.class, "secret", getInstance());
    }

    @Override
    public long getIdLong() {
        throw new UnsupportedOperationException();
    }

    public Bot getBot() {
        return bot;
    }

    @NotNull
    @Override
    public String getId() {
        return ReflectionUtils.getFieldValue(OAuthBuilder.class, "id", getInstance());
    }

    public void setRedirectURI(String domain) {
        getInstance().setRedirectURI(domain);
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public String[] getScopes() {
        return scopes;
    }

    public void setScopes(String[] scopes) {
        getInstance().setScopes(scopes);
        this.scopes = scopes;
    }
}
