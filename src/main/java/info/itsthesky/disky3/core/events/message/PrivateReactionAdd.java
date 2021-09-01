package info.itsthesky.disky3.core.events.message;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.MessageEvent;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;

public class PrivateReactionAdd extends DiSkyEvent<PrivateMessageReactionAddEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", PrivateReactionAdd.class, EvtPrivateReactionAdd.class,
                "private reaction add")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");

       EventValues.registerEventValue(EvtPrivateReactionAdd.class, MessageReaction.class, new Getter<MessageReaction, EvtPrivateReactionAdd>() {
            @Override
            public MessageReaction get(EvtPrivateReactionAdd event) {
                return event.getJDAEvent().getReaction();
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReactionAdd.class, info.itsthesky.disky3.api.emojis.Emote.class, new Getter<info.itsthesky.disky3.api.emojis.Emote, EvtPrivateReactionAdd>() {
            @Override
            public info.itsthesky.disky3.api.emojis.Emote get(EvtPrivateReactionAdd event) {
                return info.itsthesky.disky3.api.emojis.Emote.fromReaction(event.getJDAEvent().getReactionEmote());
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReactionAdd.class, User.class, new Getter<User, EvtPrivateReactionAdd>() {
            @Override
            public User get(EvtPrivateReactionAdd event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReactionAdd.class, Bot.class, new Getter<Bot, EvtPrivateReactionAdd>() {
            @Override
            public Bot get(EvtPrivateReactionAdd event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtPrivateReactionAdd extends SimpleDiSkyEvent<PrivateMessageReactionAddEvent> implements MessageEvent {
        public EvtPrivateReactionAdd(PrivateReactionAdd event) { }

        @Override
        public MessageChannel getMessageChannel() {
            return getJDAEvent().getChannel();
        }
    }

}