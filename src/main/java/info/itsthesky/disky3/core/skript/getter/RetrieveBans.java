package info.itsthesky.disky3.core.skript.getter;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RetrieveBans extends BaseMultipleRetrieveEffect<List<Guild.Ban>, Guild> {

    static {
        register(
                RetrieveBans.class,
                "bans",
                "guild"
        );
    }

    @Override
    protected RestAction<List<Guild.Ban>> retrieve(@NotNull Guild entity) {
        return entity.retrieveBanList();
    }

}
