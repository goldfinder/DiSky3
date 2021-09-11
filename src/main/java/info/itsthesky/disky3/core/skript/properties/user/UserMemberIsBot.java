package info.itsthesky.disky3.core.skript.properties.user;

import info.itsthesky.disky3.api.skript.properties.UserMemberCondition;
import net.dv8tion.jda.api.entities.User;

public class UserMemberIsBot extends UserMemberCondition {

    static {
        register(
                UserMemberIsBot.class,
                PropertyType.BE,
                "[a] [discord] bot",
                "user/member"
        );
    }

    @Override
    protected String getPropertyName() {
        return "a discord bot";
    }

    @Override
    public boolean check(User user) {
        return user.isBot();
    }
}
