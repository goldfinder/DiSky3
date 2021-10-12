package info.itsthesky.disky3.core.skript.commands;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.core.commands.CommandEvent;
import info.itsthesky.disky3.core.commands.CommandObject;
import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Name("Discord Command Prefixes")
@Description("Get prefixes of a discord command.")

public class ExprCommandPrefixes extends MultiplyPropertyExpression<CommandObject, String> {

    static {
        register(ExprCommandPrefixes.class, String.class,
                "[discord] [command] prefixes",
                "discordcommand"
        );
    }

    @Nullable
    @Override
    public String[] convert(CommandObject entity) {
        List<String> list = new ArrayList<>();
        for (Expression<String> expr : entity.getPrefixes()) {
            list.add(expr.getSingle(CommandEvent.lastEvent));
        }
        return list.toArray(new String[0]);
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "command prefixes";
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