package info.itsthesky.disky3.core.skript.properties.member;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.ColorRGB;
import ch.njol.skript.util.SkriptColor;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

public class MemberColor extends SimplePropertyExpression<Member, Color> {

    static {
        register(
                MemberColor.class,
                Color.class,
                "[discord] member color",
                "member"
        );
    }

    @Override
    public @NotNull Color convert(Member member) {
        return member.getRoles().isEmpty() ? SkriptColor.DARK_GREY : Utils.convert(member.getRoles().get(0).getColor());
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "discord member color";
    }

    @Override
    public @NotNull Class<? extends Color> getReturnType() {
        return Color.class;
    }
}
