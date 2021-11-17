package info.itsthesky.disky3.core.skript.properties.member;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.SkriptColor;
import info.itsthesky.disky3.api.Utils;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

@Name("Member Avatar")
@Description({"This represent the per-guild avatar of a member.",
        "If the specified member doesn't have a custom avatar, it will return its user's one."})
public class MemberAvatar extends SimplePropertyExpression<Member, String> {

    static {
        register(
                MemberAvatar.class,
                String.class,
                "[discord] member avatar",
                "member"
        );
    }

    @Override
    public @NotNull String convert(Member member) {
        return member.getEffectiveAvatarUrl();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "discord member avatar";
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }
}
