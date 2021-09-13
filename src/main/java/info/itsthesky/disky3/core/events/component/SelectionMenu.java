package info.itsthesky.disky3.core.events.component;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.InteractionEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

public class SelectionMenu extends DiSkyEvent<SelectionMenuEvent> {

    static {
        DiSkyEvent.register("Selection Menu Interact", SelectionMenu.class, EvtSelectionMenu.class,
                "(selection menu|drop[ ]down) (interact|change|select|update)")
                .setName("Selection Menu Interact");

        EventValues.registerEventValue(EvtSelectionMenu.class, SelectOption[].class, new Getter<SelectOption[], EvtSelectionMenu>() {
            @Override
            public SelectOption[] get(EvtSelectionMenu event) {
                return event.getJDAEvent().getSelectedOptions().toArray(new SelectOption[0]);
            }
        }, 0);

        EventValues.registerEventValue(EvtSelectionMenu.class, GuildChannel.class, new Getter<GuildChannel, EvtSelectionMenu>() {
            @Override
            public GuildChannel get(EvtSelectionMenu event) {
                return (GuildChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtSelectionMenu.class, Guild.class, new Getter<Guild, EvtSelectionMenu>() {
            @Override
            public Guild get(EvtSelectionMenu event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtSelectionMenu.class, Member.class, new Getter<Member, EvtSelectionMenu>() {
            @Override
            public Member get(EvtSelectionMenu event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EvtSelectionMenu.class, String.class, new Getter<String, EvtSelectionMenu>() {
            @Override
            public String get(EvtSelectionMenu event) {
                return event.getJDAEvent().getComponent().getId();
            }
        }, 0);

        EventValues.registerEventValue(EvtSelectionMenu.class, UpdatingMessage.class, new Getter<UpdatingMessage, EvtSelectionMenu>() {
            @Override
            public UpdatingMessage get(EvtSelectionMenu event) {
                return UpdatingMessage.from(event.getJDAEvent().getMessage());
            }
        }, 0);

        EventValues.registerEventValue(EvtSelectionMenu.class, User.class, new Getter<User, EvtSelectionMenu>() {
            @Override
            public User get(EvtSelectionMenu event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

        EventValues.registerEventValue(EvtSelectionMenu.class, JDA.class, new Getter<JDA, EvtSelectionMenu>() {
            @Override
            public JDA get(EvtSelectionMenu event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtSelectionMenu extends SimpleDiSkyEvent<SelectionMenuEvent> implements InteractionEvent {

        public EvtSelectionMenu(SelectionMenu event) { }

        @Override
        public GenericInteractionCreateEvent getInteractionEvent() {
            return getJDAEvent();
        }
    }


}