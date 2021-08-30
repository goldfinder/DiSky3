package info.itsthesky.disky3.core.skript;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.messages.MessageHelper;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.WaiterBotEffect;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Edit Message")
@Description({"Edit a discord message. The rest of the code will be executed when Discord will agreed the message edit action!"})
@Examples({"edit {_msg} to show last embed",
"edit {_msg} to show \"Hello World :p\""})
@Since("3.0")
public class EffEditMessage extends WaiterBotEffect {

    static {
        register(
                EffEditMessage.class,
                "edit [the] [message] %message% (with|to show) %embedbuilder/string/messagebuilder%"
        );
    }

    private Expression<UpdatingMessage> exprMessage;
    private Expression<Object> exprToReplaceWith;

    @Override
    @SuppressWarnings("unchecked")
    public boolean initEffect(Expression[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.exprMessage = (Expression<UpdatingMessage>) exprs[0];
        this.exprToReplaceWith = (Expression<Object>) exprs[1];
        return true;
    }

    @Override
    public void runEffect(Event e) {
        UpdatingMessage message = exprMessage.getSingle(e);
        Object content = exprToReplaceWith.getSingle(e);
        if (message == null || content == null) return;

        if (getUsedBot() != null) {

            Bot bot = (Bot) getUsedBot().getSingle(e);
            if (bot == null) return;
            for (TextChannel channel : bot.getCore().getTextChannels()) {
                if (channel.compareTo(message.getMessage().getTextChannel()) == 0) {

                    channel.retrieveMessageById(message.getID()).queue(
                            msg -> message.getMessage().editMessage(Utils.parseMessageContent(content).build()).queue(t -> {
                                restart();
                            }, ex -> {
                                DiSky.exception(ex, getNode());
                                restart();
                            })
                    );

                }
            }

        } else {
            message.getMessage().editMessage(Utils.parseMessageContent(content).build()).queue(t -> {
                restart();
            }, ex -> {
                DiSky.exception(ex, getNode());
                restart();
            });
        }
    }

    @Override
    public String toStringEffect(@Nullable Event e, boolean debug) {
        return "edit message " + exprMessage.toString(e, debug) + " to show " + exprToReplaceWith.toString(e, debug);
    }
}
