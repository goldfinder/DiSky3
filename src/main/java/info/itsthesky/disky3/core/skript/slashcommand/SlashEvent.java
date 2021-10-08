package info.itsthesky.disky3.core.skript.slashcommand;

import info.itsthesky.disky3.api.skript.events.specific.InteractionEvent;
import info.itsthesky.disky3.core.skript.slashcommand.api.SlashObject;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SlashEvent extends Event implements Cancellable, InteractionEvent {

    public static SlashEvent lastEvent;

    private final SlashObject command;
    private final Guild guild;
    private final User user;
    private final Member member;
    private final MessageChannel messagechannel;
    private boolean cancelled;
    private final GuildChannel channel;
    private final JDA bot;
    private final List<OptionMapping> arguments;
    private SlashCommandEvent event;

    public SlashCommandEvent getJDAEvent() {
        return event;
    }

	public SlashEvent(SlashCommandEvent event,
                      SlashObject command, List<OptionMapping> arguments, Guild guild,
                      MessageChannel messagechannel, GuildChannel channel, User user,
                      Member member, JDA bot) {
	    this.arguments = arguments;
        this.command = command;
        this.guild = guild;
        this.user = user;
        this.member = member;
        this.channel = channel;
        this.messagechannel = messagechannel;
        this.bot = bot;
        this.event = event;
        lastEvent = this;
    }

    public static SlashEvent getLastEvent() {
        return lastEvent;
    }

    public static void setLastEvent(SlashEvent lastEvent) {
        SlashEvent.lastEvent = lastEvent;
    }

    public SlashObject getCommand() {
        return command;
    }

    public Guild getGuild() {
        return guild;
    }

    public User getUser() {
        return user;
    }

    public Member getMember() {
        return member;
    }

    public MessageChannel getMessagechannel() {
        return messagechannel;
    }

    public GuildChannel getChannel() {
        return channel;
    }

    public JDA getBot() {
        return bot;
    }

    public List<OptionMapping> getArguments() {
        return arguments;
    }

    public SlashCommandEvent getEvent() {
        return event;
    }

    public void setEvent(SlashCommandEvent event) {
        this.event = event;
    }

    public static HandlerList getHANDLERS() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public GenericInteractionCreateEvent getInteractionEvent() {
        return getEvent();
    }
}
