package info.itsthesky.disky3.core.skript.properties.thread;

import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import net.dv8tion.jda.api.entities.GuildThread;
import net.dv8tion.jda.api.entities.Member;

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
