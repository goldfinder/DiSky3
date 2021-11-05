package info.itsthesky.disky3.core.skript.properties.guild;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.changers.ChangeablePropertyExpression;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class GuildBoostRole extends ChangeablePropertyExpression<Guild, Role> {

    static {
        register(
                GuildBoostRole.class,
                Role.class,
                "[discord] boost role",
                "guild"
        );
    }

    private NodeInformation info;

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean diskyChanger) {
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode) {

    }

    @Override
    protected Role @NotNull [] get(@NotNull Event e, Guild @NotNull [] source) {
        return Arrays
                .stream(source)
                .map(Guild::getBoostRole)
                .toArray(Role[]::new);
    }

    @Override
    public @NotNull Class<? extends Role> getReturnType() {
        return Role.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "boost role of " + getExpr().toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends Guild>) exprs[0]);
        info = new NodeInformation();
        return true;
    }
}
