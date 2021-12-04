package info.itsthesky.disky3.core.skript.getter;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;

public class RetrieveOwner extends BaseRetrieveEffect<Member, Guild> {

    static {
        register(
                RetrieveOwner.class,
                "owner",
                "guild",
                false, true
        );
    }

    @Override
    protected boolean requireID() {
        return false;
    }

    @Override
    protected RestAction<Member> retrieve(@NotNull String input, @NotNull Guild entity) {
        return entity.retrieveOwner();
    }

}
