package info.itsthesky.disky3.core.skript.properties.role;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.util.Date;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.skript.action.ActionProperty;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RoleCreationDate extends ActionProperty<Role, RoleAction, Date> {

    static {
        register(
                RoleCreationDate.class,
                Date.class,
                "[discord] [role] creation (age|date)",
                "role/roleaction"
        );
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean diskyChanger) {
        return CollectionUtils.array();
    }

    @Override
    public @NotNull Class<? extends Date> getReturnType() {
        return Date.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "discord creation date of " + getExpr().toString(e, debug);
    }

    @Override
    public void change(Role role, Date value) { }

    @Override
    public RoleAction change(RoleAction action, Date value) {
        return null;
    }

    @Override
    public Date get(Role role) {
        return Utils.convert(role.getTimeCreated());
    }
}
