package info.itsthesky.disky3.core.skript.properties.channels;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.util.Color;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.skript.action.ActionProperty;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChannelParent extends ActionProperty<GuildChannel, ChannelAction, Category> {

    static {
        register(
                ChannelParent.class,
                Category.class,
                "[channel] parent",
                "channel/channelaction"
        );
    }


    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean diskyChanger) {
        return mode.equals(Changer.ChangeMode.SET) ? new Class[] {Category.class} : new Class[0];
    }

    @Override
    public void change(GuildChannel role, Category value) {
        role.getManager().setParent(value).queue();
    }

    @Override
    public ChannelAction change(ChannelAction action, Category value) {
        return action.setParent(value);
    }

    @Override
    public Category get(GuildChannel role) {
        return ((ICategorizableChannel) role).getParentCategory();
    }

    @Override
    public @NotNull Class<? extends Category> getReturnType() {
        return Category.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "category parent";
    }
}
