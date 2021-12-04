package info.itsthesky.disky3.core.skript.getter;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RetrieveUser extends BaseRetrieveEffect<User, Bot> {

    static {
        register(
                RetrieveUser.class,
                "user",
                "bot",
                true, false
        );
    }

    @Override
    protected boolean allowCustomBot() {
        return false;
    }

    @Override
    protected RestAction<User> retrieve(@NotNull String input, @NotNull Bot entity) {
        return entity.getCore().retrieveUserById(input);
    }
}
