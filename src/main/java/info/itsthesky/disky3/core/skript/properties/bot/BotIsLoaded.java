package info.itsthesky.disky3.core.skript.properties.bot;

import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.PropertyCondition;
import org.jetbrains.annotations.NotNull;

public class BotIsLoaded extends PropertyCondition<String> {

    static {
        register(
                BotIsLoaded.class,
                PropertyType.BE,
                "[been] loaded (in|on|from|over) discord",
                "string"
        );
    }

    @Override
    public boolean check(@NotNull String name) {
        return BotManager.isLoaded(name);
    }

    @Override
    protected String getPropertyName() {
        return "loaded";
    }
}
