package info.itsthesky.disky3.core.skript.properties.member;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.BaseGuildMessageChannel;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MemberPermissions extends SimpleExpression<Permission> {

    static {
        Skript.registerExpression(
                MemberPermissions.class,
                Permission.class,
                ExpressionType.COMBINED,
                "[the] permissions of %member% in [the] [channel] %channel%",
                "[the] %member%'[s] permissions in [the] [channel] %channel%"
        );
    }

    private Expression<Member> exprMember;
    private Expression<GuildChannel> exprChannel;

    @Override
    protected Permission @NotNull [] get(@NotNull Event e) {
        final Member member = exprMember.getSingle(e);
        final BaseGuildMessageChannel channel = (BaseGuildMessageChannel) exprChannel.getSingle(e);
        if (member == null || channel == null)
            return new Permission[0];
        return member.getPermissions(channel).toArray(new Permission[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends Permission> getReturnType() {
        return Permission.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "permissions of " + exprMember.toString(e, debug) + " in channel " + exprChannel.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprMember = (Expression<Member>) exprs[0];
        exprChannel = (Expression<GuildChannel>) exprs[1];
        return true;
    }
}
