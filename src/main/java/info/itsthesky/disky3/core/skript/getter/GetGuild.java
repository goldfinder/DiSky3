package info.itsthesky.disky3.core.skript.getter;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.BotExpression;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GetGuild extends BotExpression<Guild> {

    static {
        register(
                GetGuild.class,
                Guild.class,
                "guild (with|from) id %string% [(with|using) [bot] %-bot%]"
        );
    }

    private Expression<String> exprId;
    private Expression<Bot> exprBot;
    private NodeInformation node;

    @Override
    public boolean initExpr(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        node = new NodeInformation();
        exprId = (Expression<String>) exprs[0];
        exprBot = (Expression<Bot>) exprs[1];

        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);

        if (exprBot == null) {
            DiSky.exception(new DiSkyException("The bot cannot be retrieved in the get guild expression!"), node);
            return false;
        }

        return true;
    }

    @Override
    public String toStringExpr(@Nullable Event e, boolean debug) {
        return "guild with id " + exprId.toString(e, debug);
    }

    @Override
    protected Guild[] get(@NotNull Event e) {
        String id = exprId.getSingle(e);
        Bot bot = exprBot.getSingle(e);
        if (id == null || bot == null) return new Guild[0];

        return new Guild[] {bot.getCore().getGuildById(id)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Guild> getReturnType() {
        return Guild.class;
    }
}
