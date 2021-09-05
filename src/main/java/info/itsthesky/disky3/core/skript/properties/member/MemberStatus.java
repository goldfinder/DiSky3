package info.itsthesky.disky3.core.skript.properties.member;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

public class MemberStatus extends SimplePropertyExpression<Member, OnlineStatus> {

    static {
        register(
                MemberStatus.class,
                OnlineStatus.class,
                "[discord] OnlineStatus",
                "member"
        );
    }

    private NodeInformation info;

    @Override
    public @NotNull OnlineStatus convert(Member member) {
        return member.getOnlineStatus();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "discord online status";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        info = new NodeInformation();
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @NotNull Class<? extends OnlineStatus> getReturnType() {
        return OnlineStatus.class;
    }
}
