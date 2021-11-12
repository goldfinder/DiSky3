package info.itsthesky.disky3.core.skript.getter;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.entities.ThreadChannel;
import net.dv8tion.jda.api.entities.NewsChannel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GetThread extends SimpleExpression<ThreadChannel> {

    static {
        Skript.registerExpression(
                GetThread.class,
                ThreadChannel.class,
                ExpressionType.SIMPLE,
                "thread (with|from) id %string% [(with|using) [bot] %-bot%]"
        );
    }

    private Expression<String> exprId;
    private Expression<Bot> exprBot;
    private NodeInformation node;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        node = new NodeInformation();
        exprId = (Expression<String>) exprs[0];
        exprBot = (Expression<Bot>) exprs[1];

        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);

        if (exprBot == null) {
            DiSky.exception(new DiSkyException("The bot cannot be retrieved in the get text channel expression!"), node);
            return false;
        }

        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "thread with id " + exprId.toString(e, debug);
    }

    @Override
    protected ThreadChannel @NotNull [] get(@NotNull Event e) {
        String id = exprId.getSingle(e);
        Bot bot = exprBot.getSingle(e);
        if (id == null || bot == null) return new ThreadChannel[0];

        return new ThreadChannel[] {bot.getCore().getThreadChannelById(id)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends ThreadChannel> getReturnType() {
        return ThreadChannel.class;
    }
}
