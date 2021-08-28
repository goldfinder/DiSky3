package info.itsthesky.disky3.core.events.message;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.MessageEvent;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;

public class ReactionRemove extends DiSkyEvent<GuildMessageReactionRemoveEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", ReactionRemove.class, EvtReactionRemove.class,
                "reaction remove[d]")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtReactionRemove.class, User.class, new Getter<User, EvtReactionRemove>() {
            @Override
            public User get(EvtReactionRemove event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtReactionRemove.class, Member.class, new Getter<Member, EvtReactionRemove>() {
            @Override
            public Member get(EvtReactionRemove event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

       EventValues.registerEventValue(EvtReactionRemove.class, info.itsthesky.disky.tools.object.Emote.class, new Getter<info.itsthesky.disky.tools.object.Emote, EvtReactionRemove>() {
            @Override
            public info.itsthesky.disky.tools.object.Emote get(EvtReactionRemove event) {
                return info.itsthesky.disky.tools.object.Emote.fromReaction(event.getJDAEvent().getReactionEmote());
            }
        }, 0);

       EventValues.registerEventValue(EvtReactionRemove.class, TextChannel.class, new Getter<TextChannel, EvtReactionRemove>() {
            @Override
            public TextChannel get(EvtReactionRemove event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtReactionRemove.class, UpdatingMessage.class, new Getter<UpdatingMessage, EvtReactionRemove>() {
            @Override
            public UpdatingMessage get(EvtReactionRemove event) {
                return UpdatingMessage.from(event.getJDAEvent().getChannel().retrieveMessageById(event.getJDAEvent().getMessageId()).complete());
            }
        }, 0);

       EventValues.registerEventValue(EvtReactionRemove.class, Guild.class, new Getter<Guild, EvtReactionRemove>() {
            @Override
            public Guild get(EvtReactionRemove event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtReactionRemove.class, JDA.class, new Getter<JDA, EvtReactionRemove>() {
            @Override
            public JDA get(EvtReactionRemove event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtReactionRemove extends SimpleDiSkyEvent<GuildMessageReactionRemoveEvent> implements MessageEvent {
        public EvtReactionRemove(ReactionRemove event) { }

        @Override
        public MessageChannel getMessageChannel() {
            return getJDAEvent().getChannel();
        }
    }

}