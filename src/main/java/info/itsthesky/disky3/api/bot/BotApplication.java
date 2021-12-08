package info.itsthesky.disky3.api.bot;

/**
 * Class that will store and handle information about a bot's application.
 */
public class BotApplication {

    private final String name;
    private final String clientID;
    private final String clientSecret;

    public BotApplication(String name, String clientID, String clientSecret) {
        this.name = name;
        this.clientID = clientID;
        this.clientSecret = clientSecret;
    }

    public String getName() {
        return name;
    }

    public Bot getBot() {
        return BotManager.searchFromName(getName());
    }

    public String getClientID() {
        return clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
