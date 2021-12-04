package info.itsthesky.disky3.core.skript.getter;

import info.itsthesky.disky3.api.emojis.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ListedEmote;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class RetrieveEmotes extends BaseMultipleRetrieveEffect<List<ListedEmote>, Guild> {

    static {
        register(
                RetrieveEmotes.class,
                "emotes",
                "guild"
        );
    }

    @Override
    public RestAction<List<ListedEmote>> retrieve(@NotNull Guild entity) {
        return entity.retrieveEmotes();
    }

    @Override
    protected List<?> convert(List<ListedEmote> original) {
        return original
                .stream()
                .map(Emote::fromJDA)
                .collect(Collectors.toList());
    }
}
