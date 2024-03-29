package info.itsthesky.disky3.core.events.other;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import org.jetbrains.annotations.NotNull;

public class InviteCreate extends DiSkyEvent<GuildInviteCreateEvent> {

    static {
        DiSkyEvent.register("Guild Invite Create", InviteCreate.class, EvtInviteCreate.class,
                "[discord] [guild] invite create")
                .setName("Guild Invite Create")
                .setDesc("Fired when a invite is created in a guild.")
                .setExample("on guild invite create:");


       EventValues.registerEventValue(EvtInviteCreate.class, Invite.class, new Getter<Invite, EvtInviteCreate>() {
            @Override
            public Invite get(@NotNull EvtInviteCreate event) {
                return event.getJDAEvent().getInvite();
            }
        }, 0);

       EventValues.registerEventValue(EvtInviteCreate.class, VoiceChannel.class, new Getter<VoiceChannel, EvtInviteCreate>() {
            @Override
            public VoiceChannel get(@NotNull EvtInviteCreate event) {
                return event.getJDAEvent().getVoiceChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtInviteCreate.class, TextChannel.class, new Getter<TextChannel, EvtInviteCreate>() {
            @Override
            public TextChannel get(@NotNull EvtInviteCreate event) {
                return event.getJDAEvent().getTextChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtInviteCreate.class, Category.class, new Getter<Category, EvtInviteCreate>() {
            @Override
            public Category get(@NotNull EvtInviteCreate event) {
                return event.getJDAEvent().getCategory();
            }
        }, 0);

       EventValues.registerEventValue(EvtInviteCreate.class, GuildChannel.class, new Getter<GuildChannel, EvtInviteCreate>() {
            @Override
            public GuildChannel get(@NotNull EvtInviteCreate event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtInviteCreate.class, Guild.class, new Getter<Guild, EvtInviteCreate>() {
            @Override
            public Guild get(@NotNull EvtInviteCreate event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtInviteCreate.class, Bot.class, new Getter<Bot, EvtInviteCreate>() {
            @Override
            public Bot get(@NotNull EvtInviteCreate event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtInviteCreate extends SimpleDiSkyEvent<GuildInviteCreateEvent> implements LogEvent {
        public EvtInviteCreate(InviteCreate event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}