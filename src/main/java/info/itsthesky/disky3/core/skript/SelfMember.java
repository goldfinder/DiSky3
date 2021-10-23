package info.itsthesky.disky3.core.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.bot.Bot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SelfMember extends SimpleExpression<Member> {

    static {
        Skript.registerExpression(
                SelfMember.class,
                Member.class,
                ExpressionType.SIMPLE,
                "[the] self member of [the] [bot] %bot% in [the] [guild] %guild%"
        );
    }

    private Expression<Bot> exprBot;
    private Expression<Guild> exprGuild;

    @Override
    protected Member @NotNull [] get(@NotNull Event e) {
        Bot bot = exprBot.getSingle(e);
        Guild guild = exprGuild.getSingle(e);
        if (bot == null || guild == null)
            return new Member[0];
        return new Member[] {guild.getMemberById(bot.getId())};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Member> getReturnType() {
        return Member.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "self member of bot " + exprBot.toString(e, debug) + " in guild" + exprGuild.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprBot = (Expression<Bot>) exprs[0];
        exprGuild = (Expression<Guild>) exprs[1];
        return true;
    }
}
