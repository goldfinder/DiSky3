package info.itsthesky.disky3.core.skript.properties.thread;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.entities.ThreadChannel;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ThreadMembers extends MultiplyPropertyExpression<ThreadChannel, Member> {

    static {
        register(
                ThreadMembers.class,
                Member.class,
                "[discord] [thread] members",
                "thread"
        );
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.ChangeMode mode) {
        if (mode.equals(Changer.ChangeMode.ADD) || mode.equals(Changer.ChangeMode.REMOVE))
            return CollectionUtils.array(Member.class);
        return CollectionUtils.array();
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta == null || delta.length == 0 || delta[0] == null)
            return;
        final Member[] members = (Member[]) delta;
        final ThreadChannel thread = getExpr().getSingle(e);
        if (thread == null)
            return;
        for (Member member : members) {
            if (mode == Changer.ChangeMode.ADD) {
                thread.addThreadMember(member).queue(null, ex -> DiSky.exception(ex, node));
            } else {
                thread.removeThreadMember(member).queue(null, ex -> DiSky.exception(ex, node));
            }
        }
    }

    private NodeInformation node;
    @Override
    public boolean init(Expression<?> @NotNull [] expr, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        node = new NodeInformation();
        return super.init(expr, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @NotNull Class<? extends Member> getReturnType() {
        return Member.class;
    }

    @Override
    protected String getPropertyName() {
        return "members";
    }

    @Override
    protected Member[] convert(ThreadChannel t) {
        return t.getMembers().toArray(new Member[0]);
    }
}
