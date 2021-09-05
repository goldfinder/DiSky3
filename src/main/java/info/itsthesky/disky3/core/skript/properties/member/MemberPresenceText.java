package info.itsthesky.disky3.core.skript.properties.member;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class MemberPresenceText extends SimplePropertyExpression<Member, String> {

    static {
        register(
                MemberPresenceText.class,
                String.class,
                "[discord] presence text",
                "member"
        );
    }

    private NodeInformation info;

    @Override
    public @NotNull String convert(Member member) {
        return member.getActivities().get(0).getName();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "discord presence text";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        info = new NodeInformation();
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }
}
