package info.itsthesky.disky3.core.skript.properties.role;

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
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RolePosition extends ChangeablePropertyExpression<Role, Number> {

    static {
        register(
                RolePosition.class,
                Number.class,
                "[discord] [role] position",
                "role"
        );
    }

    private NodeInformation info;

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean diskyChanger) {
        if (mode == Changer.ChangeMode.SET)
            return CollectionUtils.array(Number.class);
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0 || delta[0] == null) return;
        Role Role = Utils.verifyVar(e, getExpr(), null);
        final int value = ((Number) delta[0]).intValue();

        Role = bot.getCore().getGuildById(Role.getGuild().getId()).getRoleById(Role.getId());

        Role.getGuild().modifyRolePositions().selectPosition(Role).moveTo(value).queue(null, ex -> DiSky.exception(ex, info));
    }

    @Override
    protected Number @NotNull [] get(@NotNull Event e, Role @NotNull [] source) {
        return new Number[] {source[0].getPosition()};
    }

    @Override
    public @NotNull Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "role position of " + getExpr().toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends Role>) exprs[0]);
        info = new NodeInformation();
        return true;
    }
}
