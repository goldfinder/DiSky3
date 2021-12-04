package info.itsthesky.disky3.core.sections;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.ExprEventExpression;
import ch.njol.skript.lang.Expression;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.emojis.Emote;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.section.EventSection;
import info.itsthesky.disky3.api.skript.section.EventWaiter;
import info.itsthesky.disky3.core.events.message.ReactionAdd;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Reaction Section")
@Description("React to a specific message with one reaction. The optional code section will be ran when any member react through the emote.")
public class ReactionSection extends EventSection<MessageReactionAddEvent> {

    static {
        register(
                ReactionSection.class,
                "react to [the] [message] %message% with [the] [emote] %emotes% [to run]"
        );
    }

    private Expression<UpdatingMessage> exprMessage;
    private Expression<Emote> exprEmotes;

    @Override
    protected EventWaiter<MessageReactionAddEvent> runEffect(Event e) {
        final UpdatingMessage message = exprMessage.getSingle(e);
        final Emote emote = Utils.verifyVar(e, exprEmotes, null);
        if (emote == null || message == null)
            return null;

        final String idToCheck = message.getMessage().getId();
        Utils.addEmoteToMessage(emote, message.getMessage());

        final ReactionAdd.EvtReactionAdd event = new ReactionAdd.EvtReactionAdd(new ReactionAdd());
        return new EventWaiter<>(
                MessageReactionAddEvent.class,
                ev -> {
                    event.setJDAEvent(ev);
                    runSection(event);
                    if (event.isCancelled())
                        ev.getReaction().removeReaction(ev.getUser()).queue();
                },
                ev -> {
                    if (!Utils.areEmojiSimilar(ev.getReactionEmote(), emote))
                        return false;
                    if (!ev.getMessageId().equalsIgnoreCase(idToCheck))
                        return false;
                    return !ev.getUserId().equalsIgnoreCase(message.getMessage().getJDA().getSelfUser().getId());
                },
                emote.getId()
        );
    }

    @Override
    protected Class<? extends Event> getSectionEvent() {
        return ReactionAdd.EvtReactionAdd.class;
    }

    @Override
    protected void loadExpressions(Expression<?>[] exprs) {
        exprMessage = (Expression<UpdatingMessage>) exprs[0];
        exprEmotes = (Expression<Emote>) exprs[1];
    }

    @Override
    protected String getSectionName() {
        return "react section";
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "react with  " + exprEmotes.toString(e, debug) + " on " + exprMessage.toString(e, debug);
    }
}
