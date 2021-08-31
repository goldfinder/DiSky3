package info.itsthesky.disky3.api.changers;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.bot.Bot;
import org.bukkit.event.Event;

public abstract class ChangeableExpression implements Expression {

    @Override
    public final void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        DiSky.exception(new DiSkyException("This entity can only be changed using the Disky's changer!\nReport this to the DiSky's GitHub."), null);
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

    public abstract Class<?>[] acceptChange(Changer.ChangeMode mode, boolean vixioChanger);

    public abstract void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode);

}
