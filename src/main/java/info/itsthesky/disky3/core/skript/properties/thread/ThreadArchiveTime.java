package info.itsthesky.disky3.core.skript.properties.thread;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.changers.ChangeablePropertyExpression;
import net.dv8tion.jda.api.entities.ThreadChannel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class ThreadArchiveTime extends ChangeablePropertyExpression<ThreadChannel, String> {

    static {
        register(
                ThreadArchiveTime.class,
                String.class,
                "[discord] [max] archive (date|time)",
                "thread"
        );
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean diskyChanger) {
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode) { }

    @Override
    protected String @NotNull [] get(@NotNull Event e, ThreadChannel @NotNull [] source) {
        return new String[] {source[0].getAutoArchiveDuration().name().replaceAll("_", " ").toLowerCase(Locale.ROOT)};
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "max archive time of " + getExpr().toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends ThreadChannel>) exprs[0]);
        return true;
    }
}
