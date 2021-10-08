package info.itsthesky.disky3.api.skript.events.specific;

import net.dv8tion.jda.api.entities.User;

import java.lang.reflect.Member;

/**
 * Mean a {@link User} changed something on the logs
 */
public interface LogEvent {

    User getActionAuthor();

}
