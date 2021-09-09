package info.itsthesky.disky3.core.skript.properties.member;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.changers.ChangeablePropertyExpression;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MemberRoles extends ChangeablePropertyExpression<Member, Role> {

    static {
        register(
                MemberRoles.class,
                Role.class,
                "[discord] roles",
                "member"
        );
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean diskyChanger) {
        if (mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.REMOVE)
            return CollectionUtils.array(Role.class);
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0 || delta[0] == null) return;
        Member member = Utils.verifyVar(e, getExpr(), null);
        final Role value = Utils.tryOrDie(
                i -> (Role) i,
                delta[0],
                null
        );
        if (value == null || member == null) return;
        member = bot.getCore().getGuildById(member.getGuild().getId()).getMemberById(member.getId());

        switch (mode) {
            case ADD:
                member.getGuild().addRoleToMember(member, value).queue();
                return;
            case REMOVE:
                member.getGuild().removeRoleFromMember(member, value).queue();
                return;
        }
    }

    @Override
    protected Role @NotNull [] get(@NotNull Event e, Member @NotNull [] source) {
        return source[0].getRoles().toArray(new Role[0]);
    }

    @Override
    public @NotNull Class<? extends Role> getReturnType() {
        return Role.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "roles of " + getExpr().toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends Member>) exprs[0]);
        return true;
    }
}
