package info.itsthesky.disky3.core.skript.properties.thread;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.changers.ChangeablePropertyExpression;
import net.dv8tion.jda.api.entities.GuildThread;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ThreadParent extends ChangeablePropertyExpression<GuildThread, TextChannel> {

    static {
        register(
                ThreadParent.class,
                TextChannel.class,
                "[discord] [thread] parent",
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
    protected TextChannel @NotNull [] get(@NotNull Event e, GuildThread @NotNull [] source) {
        return new TextChannel[] {(TextChannel) source[0].getParentChannel()};
    }

    @Override
    public @NotNull Class<? extends TextChannel> getReturnType() {
        return TextChannel.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "parent of " + getExpr().toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends GuildThread>) exprs[0]);
        return true;
    }
}
