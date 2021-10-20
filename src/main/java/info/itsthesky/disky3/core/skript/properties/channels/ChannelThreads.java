package info.itsthesky.disky3.core.skript.properties.channels;

import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.GuildThread;

public class ChannelThreads extends MultiplyPropertyExpression<GuildChannel, GuildThread> {

    static {
        register(
                ChannelThreads.class,
                GuildThread.class,
                "threads",
                "channel"
        );
    }

    @Override
    public Class<? extends GuildThread> getReturnType() {
        return GuildThread.class;
    }

    @Override
    protected String getPropertyName() {
        return "threads";
    }

    @Override
    protected GuildThread[] convert(GuildChannel t) {
        return t
                .getGuild()
                .getGuildThreads()
                .stream()
                .filter(channel -> channel.getParentChannel().getId().equalsIgnoreCase(t.getId()))
                .toArray(GuildThread[]::new);
    }
}
