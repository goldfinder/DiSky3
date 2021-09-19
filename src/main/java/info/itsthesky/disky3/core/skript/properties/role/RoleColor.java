package info.itsthesky.disky3.core.skript.properties.role;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.ColorRGB;
import ch.njol.skript.util.SkriptColor;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.changers.ChangeablePropertyExpression;
import info.itsthesky.disky3.api.skript.NodeInformation;
import info.itsthesky.disky3.core.EffChange;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RoleColor extends ChangeablePropertyExpression<Role, Color> {

    static {
        register(
                RoleColor.class,
                Color.class,
                "[discord] [role] color",
                "role"
        );
    }

    private NodeInformation info;

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean diskyChanger) {
        if (mode == Changer.ChangeMode.SET)
            return CollectionUtils.array(Color.class, SkriptColor.class, ColorRGB.class);
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0 || delta[0] == null) return;
        Role Role = Utils.verifyVar(e, getExpr(), null);
        final Color value = (Color) delta[0];
        if (value == null || Role == null) return;

        Role = bot.getCore().getGuildById(Role.getGuild().getId()).getRoleById(Role.getId());

        Role.getManager().setColor(value.asBukkitColor().asRGB()).queue(null, ex -> DiSky.exception(ex, info));
    }

    @Override
    protected Color @NotNull [] get(@NotNull Event e, Role @NotNull [] source) {
        return new Color[] {new ColorRGB(
                source[0].getColor().getRed(),
                source[0].getColor().getGreen(),
                source[0].getColor().getBlue()
        )};
    }

    @Override
    public @NotNull Class<? extends Color> getReturnType() {
        return Color.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "color of " + getExpr().toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends Role>) exprs[0]);
        info = new NodeInformation();
        return true;
    }
}
