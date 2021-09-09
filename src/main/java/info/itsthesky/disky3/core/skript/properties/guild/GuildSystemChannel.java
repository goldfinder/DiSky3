package info.itsthesky.disky3.core.skript.properties.guild;

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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuildSystemChannel extends ChangeablePropertyExpression<Guild, TextChannel> {

    static {
        register(
                GuildSystemChannel.class,
                TextChannel.class,
                "[discord] system [text( |-)] channel",
                "guild"
        );
    }

    private NodeInformation info;

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean diskyChanger) {
        if (mode == Changer.ChangeMode.SET)
            return CollectionUtils.array(TextChannel.class);
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0 || delta[0] == null) return;
        Guild guild = Utils.verifyVar(e, getExpr(), null);
        final TextChannel value = (TextChannel) delta[0];
        if (value == null || guild == null) return;
        

        guild = bot.getCore().getGuildById(guild.getId());

        guild.getManager().setSystemChannel(value).queue(null, ex -> DiSky.exception(ex, info));
    }

    @Override
    protected TextChannel @NotNull [] get(@NotNull Event e, Guild @NotNull [] source) {
        return new TextChannel[] {source[0].getSystemChannel()};
    }

    @Override
    public @NotNull Class<? extends TextChannel> getReturnType() {
        return TextChannel.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "system channel of " + getExpr().toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends Guild>) exprs[0]);
        info = new NodeInformation();
        return true;
    }
}
