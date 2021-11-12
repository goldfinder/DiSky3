package info.itsthesky.disky3.core.skript.properties.channels;

import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.ThreadChannel;

public class ChannelThreads extends MultiplyPropertyExpression<GuildChannel, ThreadChannel> {

    static {
        register(
                ChannelThreads.class,
                ThreadChannel.class,
                "threads",
                "channel"
        );
    }

    @Override
    public Class<? extends ThreadChannel> getReturnType() {
        return ThreadChannel.class;
    }

    @Override
    protected String getPropertyName() {
        return "threads";
    }

    @Override
    protected ThreadChannel[] convert(GuildChannel t) {
        return t
                .getGuild()
                .getThreadChannels()
                .stream()
                .filter(channel -> channel.getParentChannel().getId().equalsIgnoreCase(t.getId()))
                .toArray(ThreadChannel[]::new);
    }
}
