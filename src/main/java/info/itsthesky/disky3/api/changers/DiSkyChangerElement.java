package info.itsthesky.disky3.api.changers;

import ch.njol.skript.classes.Changer;
import info.itsthesky.disky3.api.bot.Bot;
import org.bukkit.event.Event;

public interface DiSkyChangerElement {

    void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode);

    Class<?>[] acceptChange(Changer.ChangeMode mode, boolean diskyChanger);

}
