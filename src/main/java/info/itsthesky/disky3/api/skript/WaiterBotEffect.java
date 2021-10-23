package info.itsthesky.disky3.api.skript;

import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class WaiterBotEffect<T> extends WaiterEffect<T> {

    private final static String PREFIX = "[(with|using) [the] %-bot%]";

    private Expression<Bot> usedBot;
    protected @Nullable Expression<Bot> getUsedBot() {
        return usedBot;
    }

    public void setUsedBot(Expression<Bot> usedBot) {
        this.usedBot = usedBot;
    }

    protected boolean botDefined(Event e) {
        return usedBot != null && usedBot.getSingle(e) != null;
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
        SkriptAdapter.getInstance().setHasDelayedBefore(Kleenean.TRUE);
        this.usedBot = (Expression<Bot>) expressions[expressions.length - 1];
        return initEffect(expressions, i, kleenean, parseResult);
    }


    public abstract String toStringEffect(@Nullable Event e, boolean debug);

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return toStringEffect(e, debug) + (usedBot == null ? "" : " " + usedBot.toString(e, debug));
    }
}
