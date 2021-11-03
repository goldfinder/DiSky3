package info.itsthesky.disky3.core.skript.properties.bot;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.changers.ChangeablePropertyExpression;
import info.itsthesky.disky3.api.skript.NodeInformation;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BotUptime extends ChangeablePropertyExpression<Bot, Timespan> {

    static {
        register(
                BotUptime.class,
                Timespan.class,
                "[discord] [bot] uptime",
                "bot"
        );
    }

    private NodeInformation info;

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean diskyChanger) {
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode) { }

    @Override
    protected Timespan @NotNull [] get(@NotNull Event e, Bot @NotNull [] source) {
        return new Timespan[] {source[0].getUptime()};
    }

    @Override
    public @NotNull Class<? extends Timespan> getReturnType() {
        return Timespan.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "uptime of " + getExpr().toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends Bot>) exprs[0]);
        info = new NodeInformation();
        return true;
    }
}
