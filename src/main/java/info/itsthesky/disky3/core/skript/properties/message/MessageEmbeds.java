package info.itsthesky.disky3.core.skript.properties.message;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.properties.MultipleMessageProperty;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

@Name("Message Embeds")
@Description("Get every embeds of a specific messages. Keep in mind only webhook are allowed to send more than one embed!")
@Since("3.0")
@Examples("embeds of event-message")
public class MessageEmbeds extends MultipleMessageProperty<EmbedBuilder> {

    static {
        register(
                MessageEmbeds.class,
                EmbedBuilder.class,
                "[discord] [message] embeds"
        );
    }

    @Override
    public EmbedBuilder[] converting(UpdatingMessage original) {
        return original
                .getMessage()
                .getEmbeds()
                .stream()
                .map(EmbedBuilder::new)
                .toArray(EmbedBuilder[]::new);
    }

}
