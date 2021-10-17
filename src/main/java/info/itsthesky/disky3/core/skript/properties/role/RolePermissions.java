package info.itsthesky.disky3.core.skript.properties.role;

import ch.njol.skript.classes.Changer;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.api.skript.NodeInformation;
import info.itsthesky.disky3.api.skript.action.MultipleActionProperty;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RolePermissions extends MultipleActionProperty<Role, RoleAction, Permission> {

    static {
        register(
                RolePermissions.class,
                Permission.class,
                "[discord] [role] permission[s]",
                "role/roleaction"
        );
    }

    private NodeInformation info;

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean diskyChanger) {
        if (mode == Changer.ChangeMode.SET)
            return CollectionUtils.array(Permission[].class);
        return CollectionUtils.array();
    }

    @Override
    public @NotNull Class<? extends Permission> getReturnType() {
        return Permission.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "role permissions of " + getExpr().toString(e, debug);
    }


    @Override
    public void change(Role role, Permission[] value) {
        role.getManager().setPermissions(value).queue();
    }

    @Override
    public RoleAction change(RoleAction action, Permission[] value) {
        return action.setPermissions(value);
    }

    @Override
    public Permission[] get(Role role) {
        return role.getPermissions().toArray(new Permission[0]);
    }
}
