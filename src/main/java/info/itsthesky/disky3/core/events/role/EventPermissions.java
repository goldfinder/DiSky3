package info.itsthesky.disky3.core.events.role;

import info.itsthesky.disky3.api.skript.MultipleEventValue;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class EventPermissions extends MultipleEventValue<Permission, SimpleDiSkyEvent> {

    static {
        register(
                EventPermissions.class,
                Permission.class,
                "permissions"
        );
    }

    @Override
    protected List<Permission> convert(SimpleDiSkyEvent event, ValueAge age) {
        switch (age) {
            case PAST:
                return Arrays.asList(((RolePermissions.EvtRolePermissions) event).getJDAEvent().getOldPermissions().toArray(new Permission[0]));
            case CURRENT:
            case FUTURE:
                return Arrays.asList(((RolePermissions.EvtRolePermissions) event).getJDAEvent().getNewPermissions().toArray(new Permission[0]));
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    protected String getEntityName() {
        return "permissions";
    }

    @Override
    protected List<ValueAge> getValueAge() {
        return Arrays.asList(ValueAge.CURRENT, ValueAge.PAST, ValueAge.FUTURE);
    }

    @Override
    protected Class<SimpleDiSkyEvent>[] getEventClasses() {
        return new Class[] {RolePermissions.EvtRolePermissions.class};
    }

    @Override
    public @NotNull Class<? extends Permission> getReturnType() {
        return Permission.class;
    }
}
