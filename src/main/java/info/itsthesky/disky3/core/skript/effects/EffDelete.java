package info.itsthesky.disky3.core.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Configuration;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EffDelete extends WaiterEffect {

    static {
        final String pattern = Configuration.PARSING_DELETE_EFFECT.get() ? "discord" : "[discord]";
        Skript.registerEffect(
                EffDelete.class,
                "delete [the] "+pattern+" [entity] %messages/channels/roles/categories/invites% [(with|using) [bot] %-bot%]"
        );
    }

    private Expression<Object> exprEntity;
    private Expression<Bot> exprBot;

    @Override
    public boolean initEffect(Expression[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {

        exprEntity = (Expression<Object>) exprs[0];
        exprBot = (Expression<Bot>) exprs[1];
        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);
        if (exprBot == null) {
            Skript.error("Unable to found the bot in a delete entity effect.");
            return false;
        }

        return true;
    }

    @Override
    public void runEffect(Event e) {
        Object[] entities = exprEntity.getArray(e);
        Bot bot = exprBot.getSingle(e);
        if (bot == null || entities.length == 0) {
            restart();
            return;
        }

        if (entities instanceof UpdatingMessage[]) {
            List<Message> msg = Arrays.stream((UpdatingMessage[]) entities)
                    .map(UpdatingMessage::getMessage)
                    .collect(Collectors.toList());
            if (msg.size() == 1) {
                msg.get(0).delete().queue(v -> restart());
            } else {
                msg.get(0).getTextChannel().deleteMessages(msg).queue(v -> restart());
            }
            return;
        }

        for (Object entity : entities) {
            RestAction<Void> deleteQueue;

            if (entity instanceof UpdatingMessage) {
                deleteQueue = ((UpdatingMessage) entity).getMessage().delete();
            } else if (entity instanceof GuildChannel) {
                deleteQueue = ((GuildChannel) entity).delete();
            } else if (entity instanceof Invite) {
                deleteQueue = ((Invite) entity).delete();
            } else {
                continue;
            }

            deleteQueue.queue(v -> restart());
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "delete discord entity " + exprEntity.toString(e, debug) + " using bot " + exprBot.toString(e, debug);
    }
}
