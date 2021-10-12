package info.itsthesky.disky3.core.skript.commands;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.core.commands.CommandObject;
import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Discord Command Permissions")
@Description("Get permissions of a discord command.")

public class ExprCommandPermissions extends MultiplyPropertyExpression<CommandObject, String> {

    static {
        register(ExprCommandPermissions.class, String.class,
                "[discord] [command] permission[s]",
                "discordcommand"
        );
    }

    @Nullable
    @Override
    public String[] convert(CommandObject entity) {
        return entity.getPerms().toArray(new String[0]);
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "command permissions";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return CollectionUtils.array();
    }

    @Override
    public void change(@NotNull Event e, @Nullable Object[] delta, Changer.@NotNull ChangeMode mode) {

    }
}