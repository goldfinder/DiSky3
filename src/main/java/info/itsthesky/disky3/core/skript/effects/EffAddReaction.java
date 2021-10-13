package info.itsthesky.disky3.core.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.emojis.Emote;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public class EffAddReaction extends Effect {

    static {
        Skript.registerEffect(EffAddReaction.class,
                "(add|append) %emotes% to [(message|reactions of)] %message% [(with|using) [the] [bot] %-bot%]");
    }

    private Expression<Emote> exprEmote;
    private Expression<UpdatingMessage> exprMessage;
    private Expression<Bot> exprBot;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        this.exprEmote = (Expression<Emote>) exprs[0];
        this.exprMessage = (Expression<UpdatingMessage>) exprs[1];
        this.exprBot = (Expression<Bot>) exprs[2];
        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);
        return true;
    }

    @Override
    protected void execute(@NotNull Event e) {
        UpdatingMessage message = exprMessage.getSingle(e);
        Emote[] emotes = Utils.verifyVars(e, exprEmote, new Emote[0]);
        Bot bot = Utils.verifyVar(e, exprBot, BotManager.getLoadedBots().get(0));
        if (message == null || emotes.length == 0) return;
        Message message1 = message.getMessage();
        if (bot != null && !bot.getCore().equals(message.getMessage().getJDA()))
            message1 = bot.getCore().getTextChannelById(message.getMessage().getTextChannel().getId()).getHistory().getMessageById(message1.getId());

        for (Emote emote : emotes) {
            if (emote == null)
                continue;
            Utils.addEmoteToMessage(emote, message1);
        }
    }

    @Override
    public @NotNull String toString(Event e, boolean debug) {
        return "add reaction " + exprEmote.toString(e, debug) + " to message " + exprMessage.toString(e, debug) + " with " + exprBot.toString(e, debug);
    }

}