package info.itsthesky.disky3.core.skript.commands;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.core.commands.CommandFactory;
import info.itsthesky.disky3.core.commands.CommandObject;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Registered Commands")
@Description("Get all discord command registered in the server (minecraft server, not the guild)")

public class ExprRegisteredCommands extends SimpleExpression<CommandObject> {

    static {
        Skript.registerExpression(ExprRegisteredCommands.class, CommandObject.class, ExpressionType.SIMPLE,
                "(registered|all) [discord] (command[s]|cmd[s])"
        );
    }


    @Override
    protected CommandObject @NotNull [] get(@NotNull Event e) {
        List<CommandObject> objects = new ArrayList<>(CommandFactory.getInstance().commandMap.values());
        return objects.toArray(new CommandObject[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends CommandObject> getReturnType() {
        return CommandObject.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "all discord commands";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }
}