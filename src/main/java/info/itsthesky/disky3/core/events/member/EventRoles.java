package info.itsthesky.disky3.core.events.member;

import info.itsthesky.disky3.api.skript.MultipleEventValue;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unchecked")
public class EventRoles extends MultipleEventValue<Role, SimpleDiSkyEvent> {

    static {
        register(
                EventRoles.class,
                Role.class,
                "roles"
        );
    }

    @Override
    protected List<Role> convert(SimpleDiSkyEvent event, ValueAge age) {
        if (event instanceof MemberRoleAdd.EvtMemberRoleAdd) {
            return ((MemberRoleAdd.EvtMemberRoleAdd) event).getJDAEvent().getRoles();
        } else {
            return ((MemberRoleRemove.EvtMemberRoleRemove) event).getJDAEvent().getRoles();
        }
    }

    @Override
    protected String getEntityName() {
        return "roles";
    }

    @Override
    protected Class<SimpleDiSkyEvent>[] getEventClasses() {
        return new Class[] {MemberRoleAdd.EvtMemberRoleAdd.class, MemberRoleRemove.EvtMemberRoleRemove.class};
    }

    @Override
    public @NotNull Class<? extends Role> getReturnType() {
        return Role.class;
    }
}
