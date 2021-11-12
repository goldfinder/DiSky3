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

public class MemberCanInteract extends EasyPropertyCondition<Member> {

    static {
        register(
                MemberCanInteract.class,
                PropertyCondition.PropertyType.CAN,
                "(manage|interact) (with|to) [the] [(member|role)] %member/role%",
                "member"
        );
    }

    private Expression<Object> exprEntity;

    @Override
    public boolean check(Event e, Member member) {
        final Object entity = exprEntity.getSingle(e);
        if (entity == null) return false;
        if (entity instanceof Role) {
            return isNegated() != member.canInteract((Role) entity);
        } else {
            return isNegated() != member.canInteract((Member) entity);
        }
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprEntity = (Expression<Object>) exprs[1];
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }
}
