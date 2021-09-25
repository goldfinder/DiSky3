package info.itsthesky.disky3.core.skript.errorhandler;


import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.DiSkyRuntimeException;
import info.itsthesky.disky3.api.section.EffectSectionException;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class SectionTry extends EffectSectionException {

    public static final HashMap<Node, SectionCatch> relatedCatches = new HashMap<>();

    static {
        Skript.registerCondition(
                SectionTry.class,
                "try [the] [code]"
        );
    }

    private Node node;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (!hasSection())
            return false;
        if (checkIfCondition())
            return false;
        node = SkriptLogger.getNode();
        loadSection("try catch part", false, (ex, e) -> {
            SectionCatch catchCode = relatedCatches.get(node);
            System.out.println(catchCode);
            if (catchCode == null) {
                Skript.error("You are using a try / catch through DiSky, but without a catch! (And your code gave an exception)");
            } else {
                catchCode.catchException(e, ((DiSkyRuntimeException) ex).getThrowable());
            }
        }, ScriptLoader.getCurrentEvents());
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event e) {
        System.out.println("ye");
        try {
            runSection(e);
            return getNext();
        } catch (Throwable ex ) {
            return getNext();
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "try";
    }

    @Override
    protected void execute(Event e) {
        walk(e);
    }
}
