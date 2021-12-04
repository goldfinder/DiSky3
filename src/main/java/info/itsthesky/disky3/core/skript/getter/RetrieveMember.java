package info.itsthesky.disky3.core.skript.getter;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;

public class RetrieveMember extends BaseRetrieveEffect<Member, Guild> {

    static {
        register(
                RetrieveMember.class,
                "member",
                "guild"
        );
    }

    @Override
    protected RestAction<Member> retrieve(@NotNull String input, @NotNull Guild entity) {
        return entity.retrieveMemberById(input);
    }

}
