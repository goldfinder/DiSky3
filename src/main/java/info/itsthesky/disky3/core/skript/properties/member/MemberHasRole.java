package info.itsthesky.disky3.core.skript.properties.member;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.skript.PropertyCondition;
import info.itsthesky.disky3.api.skript.properties.base.EasyPropertyCondition;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public class MemberHasRole extends EasyPropertyCondition<Member> {

    static {
        register(
                MemberHasRole.class,
                PropertyCondition.PropertyType.HAVE,
                "[the] role %role%",
                "member"
        );
    }

    private Expression<Role> exprRole;

    @Override
    public boolean check(Event e, Member entity) {
        Role role = exprRole.getSingle(e);
        if (role == null) return false;
        return entity.getRoles().stream().anyMatch(role1 -> role1.compareTo(role) == 0);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprRole = (Expression<Role>) exprs[1];
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }
}
