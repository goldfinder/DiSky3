package info.itsthesky.disky3.api.skript.events.specific;

import net.dv8tion.jda.api.entities.MessageChannel;

/**
 * Mean the event is related to a message event with a {@link MessageChannel}
 */
public interface MessageEvent {

    MessageChannel getMessageChannel();

}
