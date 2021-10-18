package info.itsthesky.disky3.core.events.component;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.InteractionEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.jetbrains.annotations.NotNull;

public class SelectionMenu extends DiSkyEvent<SelectionMenuEvent> {

    static {
        DiSkyEvent.register("Selection Menu Interact", SelectionMenu.class, EvtSelectionMenu.class,
                "(selection menu|drop[ ]down) (interact|change|select|update)")
                .setName("Selection Menu Interact")
        .setDesc("Fired when any dropdown is clicked, and the button's message belong to the specified bot.\nUse event-dropdown to get back the dropdown ID.")
                .setExample("on dropdown interact:");

        EventValues.registerEventValue(EvtSelectionMenu.class, SelectOption[].class, new Getter<SelectOption[], EvtSelectionMenu>() {
            @Override
            public SelectOption[] get(@NotNull EvtSelectionMenu event) {
                return event.getJDAEvent().getSelectedOptions().toArray(new SelectOption[0]);
            }
        }, 0);

        EventValues.registerEventValue(EvtSelectionMenu.class, GuildChannel.class, new Getter<GuildChannel, EvtSelectionMenu>() {
            @Override
            public GuildChannel get(@NotNull EvtSelectionMenu event) {
                return (GuildChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtSelectionMenu.class, TextChannel.class, new Getter<TextChannel, EvtSelectionMenu>() {
            @Override
            public TextChannel get(@NotNull EvtSelectionMenu event) {
                return (TextChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtSelectionMenu.class, Guild.class, new Getter<Guild, EvtSelectionMenu>() {
            @Override
            public Guild get(@NotNull EvtSelectionMenu event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtSelectionMenu.class, Member.class, new Getter<Member, EvtSelectionMenu>() {
            @Override
            public Member get(@NotNull EvtSelectionMenu event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EvtSelectionMenu.class, String.class, new Getter<String, EvtSelectionMenu>() {
            @Override
            public String get(@NotNull EvtSelectionMenu event) {
                return event.getJDAEvent().getComponent().getId();
            }
        }, 0);

        EventValues.registerEventValue(EvtSelectionMenu.class, UpdatingMessage.class, new Getter<UpdatingMessage, EvtSelectionMenu>() {
            @Override
            public UpdatingMessage get(@NotNull EvtSelectionMenu event) {
                return UpdatingMessage.from(event.getJDAEvent().getMessage());
            }
        }, 0);

        EventValues.registerEventValue(EvtSelectionMenu.class, User.class, new Getter<User, EvtSelectionMenu>() {
            @Override
            public User get(@NotNull EvtSelectionMenu event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

        EventValues.registerEventValue(EvtSelectionMenu.class, JDA.class, new Getter<JDA, EvtSelectionMenu>() {
            @Override
            public JDA get(@NotNull EvtSelectionMenu event) {
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