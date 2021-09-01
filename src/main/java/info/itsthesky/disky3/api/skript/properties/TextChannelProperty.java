package info.itsthesky.disky3.api.skript.properties;

import info.itsthesky.disky3.api.messages.UpdatingMessage;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

/**
 * @author ItsTheSky
 */
public abstract class TextChannelProperty<T> extends CustomProperty<TextChannel, T> {

    @Override
    protected @NotNull String getPropertyName() {
        return "textchannel";
    }

}
