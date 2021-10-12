package info.itsthesky.disky3.api.wrapper;

import info.itsthesky.disky3.core.events.member.MemberJoin;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InviteTracking extends ListenerAdapter
{
    private final Map<String, InviteData> inviteCache = new ConcurrentHashMap<>();


    @Override
    public void onGuildInviteCreate(final GuildInviteCreateEvent event)
    {
        final String code = event.getCode();
        final InviteData inviteData = new InviteData(event.getInvite());
        inviteCache.put(code, inviteData);
    }

    @Override
    public void onGuildInviteDelete(final GuildInviteDeleteEvent event)
    {
        final String code = event.getCode();
        inviteCache.remove(code);
    }

    @Override
    public void onGuildMemberJoin(final GuildMemberJoinEvent event)
    {
        final Guild guild = event.getGuild();
        final User user = event.getUser();
        final Member selfMember = guild.getSelfMember();

        if (!selfMember.hasPermission(Permission.MANAGE_SERVER) || user.isBot())
            return;

        guild.retrieveInvites().queue(retrievedInvites ->
        {
            for (final Invite retrievedInvite : retrievedInvites)
            {
                final String code = retrievedInvite.getCode();
                final InviteData cachedInvite = inviteCache.get(code);
                if (cachedInvite == null)
                    continue;
                if (retrievedInvite.getUses() == cachedInvite.getUses())
                    continue;
                cachedInvite.incrementUses();
                MemberJoin.lastUsedInvite = retrievedInvite;
                break;
            }
        });
    }

    @Override
    public void onGuildReady(final GuildReadyEvent event)
    {
        final Guild guild = event.getGuild();
        attemptInviteCaching(guild);
    }

    @Override
    public void onGuildJoin(final GuildJoinEvent event)
    {
        final Guild guild = event.getGuild();
        attemptInviteCaching(guild);
    }

    @Override
    public void onGuildLeave(final GuildLeaveEvent event)
    {
        final long guildId = event.getGuild().getIdLong();
        inviteCache.entrySet().removeIf(entry -> entry.getValue().getGuildId() == guildId);
    }

    private void attemptInviteCaching(final Guild guild)
    {
        final Member selfMember = guild.getSelfMember();

        if (!selfMember.hasPermission(Permission.MANAGE_SERVER))
            return;

        guild.retrieveInvites().queue(retrievedInvites ->
        {
            retrievedInvites.forEach(retrievedInvite ->
                    inviteCache.put(retrievedInvite.getCode(), new InviteData(retrievedInvite)));
        });
    }
}