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
import net.dv8tion.jda.api.entities.Icon;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URL;

public class GuildSplash extends ChangeablePropertyExpression<Guild, String> {

    static {
        register(
                GuildSplash.class,
                String.class,
                "[discord] splash",
                "guild"
        );
    }

    private NodeInformation info;

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean diskyChanger) {
        if (mode == Changer.ChangeMode.SET)
            return CollectionUtils.array(String.class);
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0 || delta[0] == null) return;
        Guild guild = Utils.verifyVar(e, getExpr(), null);
        final String value = delta[0].toString();
        if (value == null || guild == null) return;

        guild = bot.getCore().getGuildById(guild.getId());

        final InputStream iconStream;
        if (Utils.isURL(value)) {
            try {
                iconStream = new URL(value).openStream();
            } catch (IOException ioException) {
                DiSky.exception(ioException, info);
                return;
            }
        } else {
            final File iconFile = new File(value);
            if (iconFile == null || !iconFile.exists())
                return;
            try {
                iconStream = new FileInputStream(iconFile);
            } catch (FileNotFoundException ex) {
                DiSky.exception(ex, info);
                return;
            }
        }

        final Icon icon;
        try {
            icon = Icon.from(iconStream);
        } catch (IOException ioException) {
            DiSky.exception(ioException, info);
            return;
        }

        guild.getManager().setSplash(icon).queue(null, ex -> DiSky.exception(ex, info));
    }

    @Override
    protected String @NotNull [] get(@NotNull Event e, Guild @NotNull [] source) {
        return new String[] {source[0].getBannerUrl()};
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "splash of " + getExpr().toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends Guild>) exprs[0]);
        info = new NodeInformation();
        return true;
    }
}
