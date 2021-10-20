package info.itsthesky.disky3.core.skript.properties.channels;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Date;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.changers.ChangeablePropertyExpression;
import net.dv8tion.jda.api.entities.GuildChannel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChannelCreationDate extends ChangeablePropertyExpression<GuildChannel, Date> {

    static {
        register(
                ChannelCreationDate.class,
                Date.class,
                "[discord] creat(ion|e[d]) (date|age|time)",
                "channel"
        );
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean diskyChanger) {
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode) { }

    @Override
    protected Date @NotNull [] get(@NotNull Event e, GuildChannel @NotNull [] source) {
        return new Date[] {Utils.convert(source[0].getTimeCreated())};
    }

    @Override
    public @NotNull Class<? extends Date> getReturnType() {
        return Date.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "creation date of " + getExpr().toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends GuildChannel>) exprs[0]);
        return true;
    }
}
