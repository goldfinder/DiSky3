package info.itsthesky.disky3.core.skript.getter;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RetrieveInvites extends BaseMultipleRetrieveEffect<List<Invite>, Guild> {

    static {
        register(
                RetrieveInvites.class,
                "invites",
                "guild"
        );
    }

    @Override
    protected RestAction<List<Invite>> retrieve(@NotNull Guild entity) {
        return entity.retrieveInvites();
    }

}
