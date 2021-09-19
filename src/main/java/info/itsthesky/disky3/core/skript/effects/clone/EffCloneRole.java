package info.itsthesky.disky3.core.skript.effects.clone;

import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.CloneEffect;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;

public class EffCloneRole extends CloneEffect<Role> {

    static {
        register(
                EffCloneRole.class,
                "role"
        );
    }

    @Override
    public AuditableRestAction<Role> clone(Role entity, Bot bot) {
        entity = bot.getCore().getRoleById(entity.getId());
        return entity.createCopy();
    }

}
