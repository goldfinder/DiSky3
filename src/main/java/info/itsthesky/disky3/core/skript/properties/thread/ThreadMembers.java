package info.itsthesky.disky3.core.skript.properties.thread;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.changers.ChangeablePropertyExpression;
import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import net.dv8tion.jda.api.entities.GuildThread;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ThreadMembers extends MultiplyPropertyExpression<GuildThread, Member> {

    static {
        register(
                ThreadMembers.class,
                Member.class,
                "[discord] members",
                "thread"
        );
    }

    @Override
    public Class<? extends Member> getReturnType() {
        return Member.class;
    }

    @Override
    protected String getPropertyName() {
        return "members";
    }

    @Override
    protected Member[] convert(GuildThread t) {
        return t.getMembers().toArray(new Member[0]);
    }
}
