package info.itsthesky.disky3.api.skript;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.bot.Bot;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class WaiterBotEffect<T> extends WaiterEffect<T> {

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
            Class<? extends Effect> effectClass,
            String... patterns
    ) {

        List<String> patterns1 = new ArrayList<>();
        for (String p : patterns) patterns1.add(p + " " + PREFIX);

        Skript.registerEffect(
                effectClass,
                patterns1.toArray(new String[0])
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        ScriptLoader.hasDelayBefore = Kleenean.TRUE;
        this.nodes = new NodeInformation();
        this.usedBot = (Expression<Bot>) expressions[expressions.length - 1];
        return initEffect(expressions, i, kleenean, parseResult);
    }


    public abstract String toStringEffect(@Nullable Event e, boolean debug);

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return toStringEffect(e, debug) + (usedBot == null ? "" : " " + usedBot.toString(e, debug));
    }
}
