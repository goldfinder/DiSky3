package info.itsthesky.disky3.core.skript.properties.role;

import ch.njol.skript.classes.Changer;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.skript.action.ActionProperty;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RolePosition extends ActionProperty<Role, RoleAction, Number> {

    static {
        register(
                RolePosition.class,
                Number.class,
                "[discord] [role] position",
                "role/roleaction"
        );
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean diskyChanger) {
        if (mode == Changer.ChangeMode.SET)
            return CollectionUtils.array(Number.class);
        return CollectionUtils.array();
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
    public void change(Role role, Number value) {
        role.getGuild().modifyRolePositions().selectPosition(role).moveTo(value.intValue()).queue();
    }

    @Override
    public RoleAction change(RoleAction action, Number value) {
        DiSky.exception(new DiSkyException("You must create the role before changing its position"), getNode());
        return null;
    }

    @Override
    public Number get(Role role) {
        return role.getPosition();
    }
}
