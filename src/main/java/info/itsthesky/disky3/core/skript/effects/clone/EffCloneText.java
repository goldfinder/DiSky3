package info.itsthesky.disky3.core.skript.effects.clone;

import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.CloneEffect;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;

public class EffCloneText extends CloneEffect<TextChannel> {

    static {
        register(
                EffCloneText.class,
                "textchannel"
        );
    }

    @Override
    public AuditableRestAction<TextChannel> clone(TextChannel entity, Bot bot) {
        entity = bot.getCore().getTextChannelById(entity.getId());
        return entity.createCopy();
    }

}
