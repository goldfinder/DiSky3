package info.itsthesky.disky3.core.skript.effects;

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
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.WaiterBotEffect;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Suppress Embed")
@Description({"Delete every embed that the message have, work same as the Discord's client features."})
@Examples({"suppress embeds of event-message"})
@Since("3.0")
public class EffSuppressEmbed extends WaiterBotEffect {

    static {
        register(
                EffSuppressEmbed.class,
                "(delete|suppress) [the] embed[s] of [the] [message] %message%"
        );
    }

    private Expression<UpdatingMessage> exprMessage;

    @Override
    @SuppressWarnings("unchecked")
    public boolean initEffect(Expression[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.exprMessage = (Expression<UpdatingMessage>) exprs[0];
        return true;
    }

    @Override
    public void runEffect(Event e) {
        UpdatingMessage message = exprMessage.getSingle(e);
        if (message == null) return;

        if (getUsedBot() != null) {

            Bot bot = (Bot) getUsedBot().getSingle(e);
            if (bot == null) return;
            for (TextChannel channel : bot.getCore().getTextChannels()) {
                if (channel.compareTo(message.getMessage().getTextChannel()) == 0) {

                    channel
                            .retrieveMessageById(message.getID())
                            .queue(
                                    msg -> message
                                            .getMessage()
                                            .suppressEmbeds(true)
                                            .queue(t -> restart(), ex -> {
                                                        DiSky.exception(ex, getNode());
                                                        restart();
                                                    }
                                            )
                    );

                }
            }

        } else {
            message
                    .getMessage()
                    .suppressEmbeds(true)
                    .queue(t -> restart(), ex -> {
                                DiSky.exception(ex, getNode());
                                restart();
                            }
                    );
        }
    }

    @Override
    public String toStringEffect(@Nullable Event e, boolean debug) {
        return "suppress embeds of " + exprMessage.toString(e, debug);
    }
}
