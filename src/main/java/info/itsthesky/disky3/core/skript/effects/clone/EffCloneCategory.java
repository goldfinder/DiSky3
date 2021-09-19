package info.itsthesky.disky3.core.skript.effects.clone;

import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.CloneEffect;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;

public class EffCloneCategory extends CloneEffect<Category> {

    static {
        register(
                EffCloneCategory.class,
                "category"
        );
    }

    @Override
    public AuditableRestAction<Category> clone(Category entity, Bot bot) {
        entity = bot.getCore().getCategoryById(entity.getId());
        return entity.createCopy();
    }

}
