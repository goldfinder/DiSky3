package info.itsthesky.disky3.api.changers;

import ch.njol.skript.classes.Changer;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.core.EffChange;
import org.jetbrains.annotations.NotNull;

public abstract class DiSkyChanger<T> implements Changer<T> {

    @Override
    public final Class<?> @NotNull [] acceptChange(@NotNull ChangeMode mode) {
        if (!EffChange.isParsing(null, true)) {
            return null;
        }
        return acceptChange(mode, true);
    }

    @Override
    public final void change(T @NotNull [] what, Object @NotNull [] delta, @NotNull ChangeMode mode) {
        if (EffChange.currentBot != null) {
            change(what, delta, EffChange.currentBot, mode);
        }
    }

    public abstract Class<?>[] acceptChange(ChangeMode mode, boolean diskyChanger);

    public abstract void change(T[] what, Object[] delta, Bot bot, Changer.ChangeMode mode);

}