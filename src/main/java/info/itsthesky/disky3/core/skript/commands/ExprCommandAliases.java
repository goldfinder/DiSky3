package info.itsthesky.disky3.core.skript.commands;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.core.commands.CommandObject;
import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Discord Command Aliases")
@Description("Get aliases of a discord command.")

public class ExprCommandAliases extends MultiplyPropertyExpression<CommandObject, String> {

    static {
        register(ExprCommandAliases.class, String.class,
                "[discord] [command] aliases",
                "discordcommand"
        );
    }

    @Override
    protected Kleenean isDelayed() {
        return Kleenean.FALSE;
    }

    @Nullable
    @Override
    public String[] convert(CommandObject entity) {
        return entity.getUsableAliases().toArray(new String[0]);
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "command aliases";
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