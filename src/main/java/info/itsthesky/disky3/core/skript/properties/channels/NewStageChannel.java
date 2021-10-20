package info.itsthesky.disky3.core.skript.properties.channels;

import info.itsthesky.disky3.api.skript.action.NewAction;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.StageChannel;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.jetbrains.annotations.NotNull;

public class NewStageChannel extends NewAction<ChannelAction> {

    static {
        register(
                NewStageChannel.class,
                ChannelAction.class,
                "stage[( |-)]channel"
        );
    }

    @Override
    protected ChannelAction create(@NotNull Guild guild) {
        return guild.createStageChannel("default channel");
    }

    @Override
    public String getNewType() {
        return "stagechannel";
    }

    @Override
    public Class<? extends ChannelAction> getReturnType() {
        return ChannelAction.class;
    }
}
