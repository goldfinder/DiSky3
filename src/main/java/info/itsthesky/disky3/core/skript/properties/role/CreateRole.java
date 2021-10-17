package info.itsthesky.disky3.core.skript.properties.role;

import info.itsthesky.disky3.api.skript.action.CreateAction;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.restaction.RoleAction;

public class CreateRole extends CreateAction<Role, RoleAction> {

    static {
        register(
                CreateRole.class,
                "roleaction"
        );
    }

}
