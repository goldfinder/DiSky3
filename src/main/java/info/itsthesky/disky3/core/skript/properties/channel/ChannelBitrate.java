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
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChannelBitrate extends ChangeablePropertyExpression<GuildChannel, Number> {

    static {
        register(
                ChannelBitrate.class,
                Number.class,
                "[discord] bitrate",
                "channel"
        );
    }

    private NodeInformation info;

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean diskyChanger) {
        if (mode == Changer.ChangeMode.SET)
            return CollectionUtils.array(Number.class);
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0 || delta[0] == null) return;
        GuildChannel channel = Utils.verifyVar(e, getExpr(), null);
        final Number value = (Number) delta[0];
        if (value == null || channel == null) return;

        channel = bot.getCore().getGuildChannelById(channel.getId());

        channel.getManager().setBitrate(value.intValue()).queue(null, ex -> DiSky.exception(ex, info));
    }

    @Override
    protected Number @NotNull [] get(@NotNull Event e, GuildChannel @NotNull [] source) {
        return new Number[] {((VoiceChannel) source[0]).getBitrate()};
    }

    @Override
    public @NotNull Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "bitrate of " + getExpr().toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends GuildChannel>) exprs[0]);
        info = new NodeInformation();
        return true;
    }
}
