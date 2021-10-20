package info.itsthesky.disky3.core.skript.properties.channels;

import info.itsthesky.disky3.api.skript.action.NewAction;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.jetbrains.annotations.NotNull;

public class NewVoiceAction extends NewAction<ChannelAction> {

    static {
        register(
                NewVoiceAction.class,
                ChannelAction.class,
                "voice[( |-)]channel"
        );
    }

    @Override
    protected ChannelAction create(@NotNull Guild guild) {
        return guild.createVoiceChannel("default channel");
    }

    @Override
    public String getNewType() {
        return "voicechannel";
    }

    @Override
    public Class<? extends ChannelAction> getReturnType() {
        return ChannelAction.class;
    }
}
