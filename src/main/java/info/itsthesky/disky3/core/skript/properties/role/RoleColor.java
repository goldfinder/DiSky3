package info.itsthesky.disky3.core.skript.properties.role;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.util.Color;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.skript.action.ActionProperty;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RoleColor extends ActionProperty<Role, RoleAction, Color> {

    static {
        register(
                RoleColor.class,
                Color.class,
                "[role] color",
                "role/roleaction"
        );
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "color";
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean diskyChanger) {
        return mode.equals(Changer.ChangeMode.SET) ? new Class[] {Color.class} : new Class[0];
    }

    @Override
    public void change(Role role, Color value) {
        role.getManager().setColor(value.asBukkitColor().asRGB()).queue();
    }

    @Override
    public RoleAction change(RoleAction action, Color value) {
        return action.setColor(value.asBukkitColor().asRGB());
    }

    @Override
    public Color get(Role role) {
        return Utils.convert(role.getColor());
    }

    @Override
    public @NotNull Class<? extends Color> getReturnType() {
        return Color.class;
    }
}
