package info.itsthesky.disky3.api.skript.properties;

import info.itsthesky.disky3.api.skript.properties.base.MultipleCustomProperty;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

/**
 * @author ItsTheSky
 */
public abstract class MultipleTextChannelProperty<T> extends MultipleCustomProperty<TextChannel, T> {

    @Override
    protected @NotNull String getPropertyName() {
        return "textchannel";
    }

    static {
        setFromType("textchannel");
    }
}
