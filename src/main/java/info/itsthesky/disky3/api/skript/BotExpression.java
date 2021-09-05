package info.itsthesky.disky3.api.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.bot.Bot;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class BotExpression<T> extends SimpleExpression<T> {

    private final static String PREFIX = "[(with|using) [the] %-bot%]";

    private NodeInformation nodes;
    protected @NotNull NodeInformation getNode() {
        return nodes;
    }

    private Expression<Bot> usedBot;
    protected @Nullable Expression<Bot> getUsedBot() {
        return usedBot;
    }

    protected static void register(
            Class exprClass,
            Class typeClass,
            String... patterns
    ) {

        List<String> patterns1 = new ArrayList<>();
        for (String p : patterns) patterns1.add(p + " " + PREFIX);

        Skript.registerExpression(
                exprClass,
                typeClass,
                ExpressionType.SIMPLE,
                patterns1.toArray(new String[0])
        );
    }

    public abstract boolean initExpr(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult);

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!initExpr(exprs, matchedPattern, isDelayed, parseResult))
            return false;
        this.nodes = new NodeInformation();
        usedBot = (Expression<Bot>) exprs[exprs.length - 1];
        return true;
    }

    public abstract String toStringExpr(@Nullable Event e, boolean debug);

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return toStringExpr(e, debug) + (usedBot != null ? " using bot " + usedBot.toString(e, debug) : "");
    }

}
