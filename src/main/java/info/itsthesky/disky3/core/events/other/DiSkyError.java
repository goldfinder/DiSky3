package info.itsthesky.disky3.core.events.other;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.BukkitEvent;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiSkyError extends SkriptEvent {

    static {
        DiSkyEvent.register("Inner Event Name", DiSkyError.class, EvtDiSkyError.class,
                "disky error")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");

        EventValues.registerEventValue(EvtDiSkyError.class, String.class, new Getter<String, EvtDiSkyError>() {
            @Override
            public String get(@NotNull EvtDiSkyError event) {
                return event.msg;
            }
        }, 0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Literal<?> @NotNull [] exprs, int i, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(@NotNull Event event) {
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "on disky error";
    }

    public static class EvtDiSkyError extends BukkitEvent {
        private final String msg;
        public EvtDiSkyError(String msg) {
            this.msg = msg;
        }
    }
}