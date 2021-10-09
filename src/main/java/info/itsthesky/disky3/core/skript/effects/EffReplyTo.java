package info.itsthesky.disky3.core.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public class EffReplyTo extends WaiterEffect<UpdatingMessage> {

    static {
        Skript.registerEffect(EffReplyTo.class,
                "reply to [the] [message] %message% (using|with|via) [message] %string/message/messagebuilder/embedbuilder% [[with] mention[ing] %-boolean%] [(with|using) %-bot%] [with [(component|row)[s]] %-buttonrows/selectbuilder%] [and store (it|the message) in %-object%]");
    }

    private Expression<UpdatingMessage> exprTarget;
    private Expression<Object> exprMessage;
    private Expression<Bot> exprBot;
    private Expression<Boolean> exprMention;
    private Expression<Object> exprRows;

    @SuppressWarnings("unchecked")
    @Override
    public boolean initEffect(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprTarget = (Expression<UpdatingMessage>) exprs[0];
        exprMessage = (Expression<Object>) exprs[1];
        exprMention = (Expression<Boolean>) exprs[2];
        exprBot = (Expression<Bot>) exprs[3];
        exprRows = (Expression<Object>) exprs[4];
        setChangedVariable(Utils.parseVar((Expression<UpdatingMessage>) exprs[5], false, true));
        if (exprBot == null)
            Utils.defaultToEventValue(exprBot, Bot.class);
        if (exprBot == null)
            Skript.error("The bot cannot be retrieved in the reply to effect!");
        return exprs[5] == null || getChangedVariable() != null;
    }

    @Override
    public void runEffect(Event e) {
        final UpdatingMessage message = exprTarget.getSingle(e);
        final Object content = exprMessage.getSingle(e);
        final Boolean mention = Utils.verifyVar(e, exprMention, false);
        final Bot bot = Utils.verifyVar(e, exprBot, BotManager.getLoadedBots().get(0));
        final ActionRow[] rows = Utils.parseComponents(Utils.verifyVars(e, exprRows, new Object[0]));
        if (bot == null || message == null || content == null)
        {
            restart();
            return;
        }
        bot
                .getCore()
                .getTextChannelById(message.getMessage().getChannel().getId())
                .retrieveMessageById(message.getID())
                .queue(msg -> {
                    final MessageBuilder builder = Utils.parseMessageContent(content);
                    message
                            .getMessage()
                            .reply(builder.build())
                            .setActionRows(rows)
                            .mentionRepliedUser(mention)
                            .queue(m -> restart(UpdatingMessage.from(m)));
                });
    }

    @Override
    public @NotNull String toString(Event e, boolean debug) {
        return "reply to the message " + exprMessage.toString(e, debug);
    }

}