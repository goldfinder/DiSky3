package info.itsthesky.disky3.core.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.emojis.Emote;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public class EffRemoveReaction extends Effect {

    static {
        Skript.registerEffect(EffRemoveReaction.class,
                "(remove|delete) %emotes% added by %user/member/bot% from %message% [(with|using) [the] [bot] %-bot%]");
    }

    private Expression<Emote> exprEmote;
    private Expression<Object> exprUser;
    private Expression<UpdatingMessage> exprUpdatingMessage;
    private Expression<Bot> exprBot;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        this.exprEmote = (Expression<Emote>) exprs[0];
        this.exprUser = (Expression<Object>) exprs[1];
        this.exprUpdatingMessage = (Expression<UpdatingMessage>) exprs[2];
        this.exprBot = Utils.defaultToEventValue((Expression<Bot>) exprs[3], Bot.class);
        return true;
    }

    @Override
    protected void execute(@NotNull Event e) {
        Object entity = exprUser.getSingle(e);
        UpdatingMessage message = exprUpdatingMessage.getSingle(e);
        final Bot bot = exprBot.getSingle(e);
        if (entity == null || message == null) return;

        User user;
        if (entity instanceof User) {
            user = (User) entity;
        } else if (entity instanceof Member) {
            user = ((Member) entity).getUser();
        } else {
            user = message.getMessage().getGuild().getMemberById(((JDA) entity).getSelfUser().getId()).getUser();
        }

        if (message.getMessage().getJDA() != bot.getCore())
            message = UpdatingMessage.from(bot.getCore().getTextChannelById(message.getMessage().getTextChannel().getId()).getHistory().getMessageById(message.getID()));

        for (MessageReaction messageReaction : message.getMessage().getReactions()) {
            for (Emote emote : this.exprEmote.getAll(e)) {
                if (Utils.areEmojiSimilar(messageReaction.getReactionEmote(), emote)) {
                    messageReaction.removeReaction(user).queue();
                }
            }
        }
    }

    @Override
    public @NotNull String toString(Event e, boolean debug) {
        return "remove "+ exprEmote.toString(e, debug) +" added by " + exprUser.toString(e, debug) + " from " + exprUpdatingMessage.toString(e, debug);
    }

}