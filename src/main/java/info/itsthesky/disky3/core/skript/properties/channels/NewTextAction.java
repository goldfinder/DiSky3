package info.itsthesky.disky3.core.skript.properties.channels;

import info.itsthesky.disky3.api.skript.action.NewAction;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
import org.jetbrains.annotations.NotNull;

public class NewTextAction extends NewAction<ChannelAction> {

    static {
        register(
                NewTextAction.class,
                ChannelAction.class,
                "text[( |-)]channel"
        );
    }

    @Override
    protected ChannelAction create(@NotNull Guild guild) {
        return guild.createTextChannel("default channel");
    }

    @Override
    public String getNewType() {
        return "textchannel";
    }

    @Override
    public Class<? extends ChannelAction> getReturnType() {
        return ChannelAction.class;
    }
}