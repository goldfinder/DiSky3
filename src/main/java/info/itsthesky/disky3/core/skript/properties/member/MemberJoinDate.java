package info.itsthesky.disky3.core.skript.properties.member;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Date;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

public class MemberJoinDate extends SimplePropertyExpression<Member, Date> {

    static {
        register(
                MemberJoinDate.class,
                Date.class,
                "[discord] join[(ed|ing)] (date|age)",
                "member"
        );
    }

    private NodeInformation info;

    @Override
    public @NotNull Date convert(Member member) {
        return Utils.convert(member.getTimeJoined());
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "join date";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        info = new NodeInformation();
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @NotNull Class<? extends Date> getReturnType() {
        return Date.class;
    }
}
