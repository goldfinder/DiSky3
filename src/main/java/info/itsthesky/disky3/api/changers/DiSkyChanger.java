package info.itsthesky.disky3.api.changers;

import ch.njol.skript.classes.Changer;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.core.EffChange;

public abstract class DiSkyChanger<T> implements Changer<T> {

    @Override
    public final Class<?>[] acceptChange(ChangeMode mode) {
        if (!EffChange.isParsing(null, true)) {
            return null;
        }
        return acceptChange(mode, true);
    }

    @Override
    public final void change(T[] what, Object[] delta, ChangeMode mode) {
        if (EffChange.currentBot != null) {
            change(what, delta, EffChange.currentBot, mode);
        }
    }

    public abstract Class<?>[] acceptChange(ChangeMode mode, boolean vixioChanger);

    public abstract void change(T[] what, Object[] delta, Bot bot, Changer.ChangeMode mode);

}