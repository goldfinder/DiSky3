package info.itsthesky.disky3.core.skript.getter;

import info.itsthesky.disky3.api.messages.UpdatingMessage;
import net.dv8tion.jda.api.entities.BaseGuildMessageChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;

public class RetrieveMessage extends BaseRetrieveEffect<Message, BaseGuildMessageChannel> {

    static {
        register(
                RetrieveMessage.class,
                "message",
                "textchannel/channel/thread"
        );
    }

    @Override
    protected RestAction<Message> retrieve(@NotNull String input, @NotNull BaseGuildMessageChannel entity) {
        return entity.retrieveMessageById(input);
    }

}
