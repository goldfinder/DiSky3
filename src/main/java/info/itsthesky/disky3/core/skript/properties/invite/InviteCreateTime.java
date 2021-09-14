package info.itsthesky.disky3.core.skript.properties.invite;

import ch.njol.skript.util.Date;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.skript.properties.InviteProperty;
import net.dv8tion.jda.api.entities.Invite;

public class InviteCreateTime extends InviteProperty<Date> {

    static {
        register(
                InviteCreateTime.class,
                Date.class,
                "[discord] [invite] creation (age|time)"
        );
    }

    @Override
    public Date converting(Invite original) {
        return Utils.convert(original.getTimeCreated());
    }
}
