package info.itsthesky.disky3.api.skript.properties;

import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.skript.PropertyCondition;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

public abstract class UserMemberCondition extends PropertyCondition<Object> {

    @Override
    public boolean check(@NotNull Object var) {
        final User user = Utils.parseUser(var);
        return check(user);
    }

    public abstract boolean check(User user);

}
