package info.itsthesky.disky3.core.skript.properties.member;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.skript.PropertyCondition;
import info.itsthesky.disky3.api.skript.properties.base.EasyPropertyCondition;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class MemberHasPermission extends EasyPropertyCondition<Member> {

    static {
        register(
                MemberHasPermission.class,
                PropertyCondition.PropertyType.HAVE,
                "[the] [discord] permission[s] %permissions% [in [the] [channel] %-channel%]",
                "member"
        );
    }

    private Expression<Permission> exprPerms;
    private Expression<GuildChannel> exprChannel;

    @Override
    public boolean check(Event e, Member entity) {
        final Permission[] perms = exprPerms.getArray(e);
        final @Nullable GuildChannel channel = Utils.verifyVar(e, exprChannel);
        if (perms.length == 0) return false;
        return channel == null ? isNegated() != entity.hasPermission(perms) : isNegated() != entity.hasPermission(channel, perms);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprPerms = (Expression<Permission>) exprs[1];
        exprChannel = (Expression<GuildChannel>) exprs[2];
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }
}
