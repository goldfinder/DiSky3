package info.itsthesky.disky3.core.skript.properties.bot;

import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import net.dv8tion.jda.api.entities.Guild;

public class BotGuilds extends MultiplyPropertyExpression<Bot, Guild> {

    static {
        register(
                BotGuilds.class,
                Guild.class,
                "guilds",
                "bot"
        );
    }

    @Override
    public Class<? extends Guild> getReturnType() {
        return Guild.class;
    }

    @Override
    protected String getPropertyName() {
        return "guilds";
    }

    @Override
    protected Guild[] convert(Bot t) {
        return t.getCore().getGuilds().toArray(new Guild[0]);
    }
}
