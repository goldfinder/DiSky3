package info.itsthesky.disky3.core.skript.effects.clone;

import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.CloneEffect;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;

public class EffCloneVoice extends CloneEffect<VoiceChannel> {

    static {
        register(
                EffCloneVoice.class,
                "voicechannel"
        );
    }

    @Override
    public AuditableRestAction<VoiceChannel> clone(VoiceChannel entity, Bot bot) {
        entity = bot.getCore().getVoiceChannelById(entity.getId());
        return entity.createCopy();
    }

}
