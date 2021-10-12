package info.itsthesky.disky3.core.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.effects.EffMessage;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Configuration;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.WaiterBotEffect;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Collectors;

public class EffSendMessage extends WaiterBotEffect<UpdatingMessage> {

    static {
        register(
                EffSendMessage.class,
                "send"+ (Configuration.PARSING_SEND_EFFECT.get() ? " discord" : "") +" [the] [message] %embedbuilder/string/messagebuilder% (in|to) [the] [channel] %channels/users/members% [with [the] (row|component)[s] %-buttonrows/selectbuilders%] [and store (it|the message) in %-object%]"
        );
    }

    private Expression<Object> exprContent;
    private Expression<Object> exprComponents;
    private Expression<Object> exprTarget;

    @Override
    @SuppressWarnings("unchecked")
    public boolean initEffect(Expression[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.exprContent = (Expression<Object>) exprs[0];
        this.exprTarget = (Expression<Object>) exprs[1];
        exprComponents = (Expression<Object>) exprs[2];
        setChangedVariable((Variable<UpdatingMessage>) exprs[3]);

        if (getUsedBot() == null)
            setUsedBot(Utils.defaultToEventValue(getUsedBot(), Bot.class));

        return true;
    }

    @Override
    public void runEffect(Event e) {
        final Object content = exprContent.getSingle(e);
        Object[] channelObjs = Utils.verifyVars(e, exprTarget, new Object[0]);
        ActionRow[] rows = Utils.parseComponents(Utils.verifyVars(e, exprComponents, new Object[0]));
        if (channelObjs.length == 0 || content == null) {
            restart();
            return;
        }
        final MessageBuilder message = Utils.parseMessageContent(content);

        for (Object channelObj : channelObjs) {
            if (channelObj instanceof User || channelObj instanceof Member) {
                final User targetUser = Utils.parseUser(channelObj);

                if (botDefined(e))
                    getUsedBot().getSingle(e).getCore().openPrivateChannelById(targetUser.getId()).queue(channel -> {
                        channel.sendMessage(message.build())
                                .setActionRows(rows).queue(msg -> {
                            restart(UpdatingMessage.from(msg));
                        });
                    });

            } else {

                if (!((GuildChannel) channelObj).getType().equals(ChannelType.TEXT)) {
                    restart();
                    return;
                }

                TextChannel channel = (TextChannel) channelObj;

                if (botDefined(e))
                    channel = getUsedBot().getSingle(e).getCore().getTextChannelById(channel.getId());

                channel
                        .sendMessage(message.build())
                        .setActionRows(rows).queue(msg -> {
                    restart(UpdatingMessage.from(msg));
                });
            }
        }
    }

    @Override
    public String toStringEffect(@Nullable Event e, boolean debug) {
        return "send message " + exprContent.toString(e, debug) + " to " + exprTarget.toString(e, debug);
    }
}
