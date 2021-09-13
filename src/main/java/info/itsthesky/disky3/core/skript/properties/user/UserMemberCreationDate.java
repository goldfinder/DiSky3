package info.itsthesky.disky3.core.skript.properties.user;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.util.Date;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.skript.properties.UserMemberProperty;
import net.dv8tion.jda.api.entities.User;

public class UserMemberCreationDate extends UserMemberProperty<Date> {

    static {
        register(
                UserMemberCreationDate.class,
                Object.class,
                "[discord] [user] creat(ion|e[d]) (date|age|time)"
        );
    }

    @Override
    public Date converting(User original) {
        return Utils.convert(original.getTimeCreated());
    }

}
