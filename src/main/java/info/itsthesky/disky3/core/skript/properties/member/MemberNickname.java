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
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MemberNickname extends ChangeablePropertyExpression<Member, String> {

    static {
        register(
                MemberNickname.class,
                String.class,
                "[discord] nick( |-)name",
                "member"
        );
    }

    private NodeInformation info;

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean vixioChanger) {
        if (mode == Changer.ChangeMode.SET)
            return CollectionUtils.array(String.class);
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0 || delta[0] == null) return;
        Member member = Utils.verifyVar(e, getExpr(), null);
        final String value = delta[0].toString();
        if (value == null || member == null) return;

        member = bot.getCore().getGuildById(member.getGuild().getId()).getMemberById(member.getId());

        member.modifyNickname(value).queue(null, ex -> DiSky.exception(ex, info));
    }

    @Override
    protected String @NotNull [] get(@NotNull Event e, Member @NotNull [] source) {
        return new String[] {source[0].getEffectiveName()};
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "nick name of " + getExpr().toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends Member>) exprs[0]);
        info = new NodeInformation();
        return true;
    }
}
