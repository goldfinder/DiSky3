package info.itsthesky.disky3.core.skript;

import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CondIsEventType extends Condition {

    static {
        Skript.registerCondition(
                CondIsEventType.class,
                "[the] [current] (event|message) is [(from|to)] [a] private [channel]",
                "[the] [current] (event|message) is [(from|to)] [a] guild [channel]"
        );
    }

    @Override
    public boolean check(@NotNull Event e) {
        GenericMessageEvent event;
        try {
            event = (GenericMessageEvent) ((SimpleDiSkyEvent<?>) e).getJDAEvent();
        } catch (Exception ex) {
            return false;
        }

        boolean value = event.isFromGuild();
        if (!toCheckFromGuild)
            value = !value;
        return isNegated() != value;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "message is from a " + (toCheckFromGuild ? "guild" : "private") + " channel";
    }

    private boolean toCheckFromGuild;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        toCheckFromGuild = matchedPattern == 1;
        //Class<? extends Event> currentEvent = SkriptAdapter.getInstance().getCurrentEvents()[0];
        return true;
    }
}
