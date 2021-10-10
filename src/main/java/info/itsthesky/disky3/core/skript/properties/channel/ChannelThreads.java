package info.itsthesky.disky3.core.skript.properties.channel;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.changers.ChangeablePropertyExpression;
import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.GuildThread;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

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
