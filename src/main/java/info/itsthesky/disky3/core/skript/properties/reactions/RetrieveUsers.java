package info.itsthesky.disky3.core.skript.properties.reactions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import info.itsthesky.disky3.core.skript.getter.BaseMultipleRetrieveEffect;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Name("Retrieve Reaction Users")
@Description({"Retrieve every users that reacted with the specified reaction.",
        "Emote (or emoji) are actual emote that could be used in messages;",
        "Reactions are what user can to with message using emote, aka they 'react' to a message, and therefore a reaction is created."})
public class RetrieveUsers extends BaseMultipleRetrieveEffect<List<User>, MessageReaction> {

    static {
        register(
                RetrieveUsers.class,
                "react[ion][s] users",
                "reaction"
        );
    }

    @Override
    protected RestAction<List<User>> retrieve(@NotNull MessageReaction entity) {
        return entity.retrieveUsers();
    }
}
