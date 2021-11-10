package info.itsthesky.disky3.api.changers;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.PropertyExpression;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import info.itsthesky.disky3.core.EffChange;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class MultipleChangeablePropertyExpression<F, T> extends MultiplyPropertyExpression<F, T> implements DiSkyChangerElement {

    @Override
    public final void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        DiSky.exception(new DiSkyException("This entity can only be changed using the Disky's changer!\nReport this to the DiSky's GitHub."));;
    }

    @Override
    public final Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (!EffChange.isParsing(this, shouldError())) {
            return null;
        }
        return acceptChange(mode, true);
    }

    public boolean shouldError() {
        return true;
    }

}
