package info.itsthesky.disky3.api.skript.properties;

import info.itsthesky.disky3.api.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

/**
 * @author ItsTheSky
 */
public abstract class UserMemberProperty<T> extends CustomProperty<Object, T> {

    static {
        setFromType("member/user");
    }

    @Override
    public @NotNull T convert(@NotNull Object entity) {
        return converting(entity);
    }

    protected User parse(Object o) {
        return Utils.parseUser(o);
    }

    public abstract T converting(User user);

    @Override
    public T converting(Object original) {
        return converting(parse(original));
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "user/member";
    }
}
