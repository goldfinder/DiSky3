package info.itsthesky.disky3.core.events.component;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.InteractionEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;

public class ButtonClick extends DiSkyEvent<ButtonClickEvent> {

    static {
        DiSkyEvent.register("Button Click", ButtonClick.class, EvtButtonClick.class,
                "button click")
                .setName("Button Click");

       EventValues.registerEventValue(EvtButtonClick.class, UpdatingMessage.class, new Getter<UpdatingMessage, EvtButtonClick>() {
            @Override
            public UpdatingMessage get(EvtButtonClick event) {
                return UpdatingMessage.from(event.getJDAEvent().getMessage());
            }
        }, 0);

       EventValues.registerEventValue(EvtButtonClick.class, GuildChannel.class, new Getter<GuildChannel, EvtButtonClick>() {
            @Override
            public GuildChannel get(EvtButtonClick event) {
                return (GuildChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtButtonClick.class, Guild.class, new Getter<Guild, EvtButtonClick>() {
            @Override
            public Guild get(EvtButtonClick event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtButtonClick.class, User.class, new Getter<User, EvtButtonClick>() {
            @Override
            public User get(EvtButtonClick event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtButtonClick.class, Member.class, new Getter<Member, EvtButtonClick>() {
            @Override
            public Member get(EvtButtonClick event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

       EventValues.registerEventValue(EvtButtonClick.class, JDA.class, new Getter<JDA, EvtButtonClick>() {
            @Override
            public JDA get(EvtButtonClick event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtButtonClick extends SimpleDiSkyEvent<ButtonClickEvent> implements InteractionEvent {
        public EvtButtonClick(ButtonClick event) { }

        @Override
        public GenericInteractionCreateEvent getInteractionEvent() {
            return getJDAEvent();
        }
    }

}