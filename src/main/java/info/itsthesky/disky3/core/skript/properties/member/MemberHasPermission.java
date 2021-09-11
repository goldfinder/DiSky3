package info.itsthesky.disky3.core.skript.properties.member;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.skript.PropertyCondition;
import info.itsthesky.disky3.api.skript.properties.base.EasyPropertyCondition;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.event.Event;

public class MemberHasPermission extends EasyPropertyCondition<Member> {

    static {
        register(
                MemberHasPermission.class,
                PropertyCondition.PropertyType.HAVE,
                "[the] permission[s] %permissions%",
                "member"
        );
    }

    private Expression<Permission> exprPerms;

    @Override
    public boolean check(Event e, Member entity) {
        final Permission[] perms = exprPerms.getArray(e);
        if (perms.length == 0) return false;
        return entity.hasPermission(perms);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprPerms = (Expression<Permission>) exprs[1];
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }
}
