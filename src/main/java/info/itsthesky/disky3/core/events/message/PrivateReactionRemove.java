package info.itsthesky.disky3.core.events.message;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.MessageEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class PrivateReactionRemove extends DiSkyEvent<MessageReactionRemoveEvent> {

    @Override
    protected Predicate<MessageReactionRemoveEvent> checker() {
        return e -> !e.isFromGuild();
    }

    static {
        DiSkyEvent.register("Private Reaction Remove", PrivateReactionRemove.class, EvtPrivateReactionAdd.class,
                "private reaction remove[d]")
                .setName("Private Reaction Remove")
                .setDesc("FIred when a reaction is removed from a private message.")
                .setExample("on private reaction remove.");

       EventValues.registerEventValue(EvtPrivateReactionAdd.class, MessageReaction.class, new Getter<MessageReaction, EvtPrivateReactionAdd>() {
            @Override
            public MessageReaction get(@NotNull EvtPrivateReactionAdd event) {
                return event.getJDAEvent().getReaction();
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReactionAdd.class, info.itsthesky.disky3.api.emojis.Emote.class, new Getter<info.itsthesky.disky3.api.emojis.Emote, EvtPrivateReactionAdd>() {
            @Override
            public info.itsthesky.disky3.api.emojis.Emote get(@NotNull EvtPrivateReactionAdd event) {
                return info.itsthesky.disky3.api.emojis.Emote.fromReaction(event.getJDAEvent().getReactionEmote());
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReactionAdd.class, User.class, new Getter<User, EvtPrivateReactionAdd>() {
            @Override
            public User get(@NotNull EvtPrivateReactionAdd event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReactionAdd.class, Bot.class, new Getter<Bot, EvtPrivateReactionAdd>() {
            @Override
            public Bot get(@NotNull EvtPrivateReactionAdd event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtPrivateReactionAdd extends SimpleDiSkyEvent<MessageReactionRemoveEvent> implements MessageEvent {
        public EvtPrivateReactionAdd(PrivateReactionRemove event) { }

        @Override
        public MessageChannel getMessageChannel() {
            return getJDAEvent().getChannel();
        }
    }

}