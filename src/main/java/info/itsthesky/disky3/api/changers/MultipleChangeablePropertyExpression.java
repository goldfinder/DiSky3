package info.itsthesky.disky3.api.changers;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.PropertyExpression;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.core.EffChange;
import org.bukkit.event.Event;

public abstract class MultipleChangeablePropertyExpression<F, T> extends PropertyExpression<F, T> {

    @Override
    public final void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        DiSky.exception(new DiSkyException("This entity can only be changed using the Disky's changer!\nReport this to the DiSky's GitHub."), null);;
    }

    @Override
    public final Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (!EffChange.isParsing(this, shouldError())) {
            return null;
        }
        return acceptChange(mode, true);
    }

    public boolean shouldError() {
        return true;
    }

    public abstract Class<?>[] acceptChange(Changer.ChangeMode mode, boolean diskyChanger);

    public abstract void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode);

}
