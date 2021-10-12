package info.itsthesky.disky3.core.skript.commands;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.core.commands.CommandObject;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Discord Command Executable In")
@Description("Get executable in of a discord command. (guild or direct message)")

public class ExprCommandExecutableIn extends SimplePropertyExpression<CommandObject, String> {

    static {
        register(ExprCommandExecutableIn.class, String.class,
                "[discord] [command] executable (state|in) [value]",
                "discordcommand"
        );
    }

    @Nullable
    @Override
    public String convert(CommandObject entity) {
        return entity.getExecutableIn().get(0).isGuild() ? "guild" : "direct message";
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
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