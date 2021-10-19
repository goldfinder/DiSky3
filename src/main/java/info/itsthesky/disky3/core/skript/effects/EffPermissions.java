package info.itsthesky.disky3.core.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class EffPermissions extends WaiterEffect {

    static {
        Skript.registerEffect(
                EffPermissions.class,
                "allow [the] [permission[s]] %permissions% to [the] [(member|role)] %member/role% [in [the] [channel] %-channel/category/textchannel/voicechannel%] [(with|using) [bot] %-bot%]",
                "(deny|revoke) [the] [permission[s]] %permissions% to [the] [(member|role)] %member/role% [in [the] [channel] %-channel/category/textchannel/voicechannel%] [(with|using) [bot] %-bot%]"
        );
    }
    
    private Expression<Permission> exprPerms;
    private Expression<Object> exprTarget;
    private Expression<GuildChannel> exprChannel;
    private Expression<Bot> exprBot;
    private boolean isRevoke = false;

    @Override
    @SuppressWarnings({"unchecked", "null"})
    public boolean initEffect(Expression[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {

        exprPerms = (Expression<Permission>) exprs[0];
        exprTarget = (Expression<Object>) exprs[1];
        exprChannel = (Expression<GuildChannel>) exprs[2];

        exprBot = (Expression<Bot>) exprs[3];
        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);
        if (exprBot == null){
            Skript.error("Unable to get the bot in a manage permission effect");
            return false;
        }

        isRevoke = i == 1;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void runEffect(Event e) {
        final List<Permission> perms = Arrays.asList(Utils.verifyVars(e, exprPerms, new Permission[0]));
        final Object entity = exprTarget.getSingle(e);
        final @Nullable GuildChannel channel = Utils.verifyVar(e, exprChannel);
        if (perms.isEmpty() || entity == null)
            return;

        if (entity instanceof Role && channel != null) {

            if (isRevoke) {
                channel.getManager().putRolePermissionOverride(
                        ((Role) entity).getIdLong(),
                        null,
                        perms
                ).queue(this::restart);
            } else {
                channel.getManager().putRolePermissionOverride(
                        ((Role) entity).getIdLong(),
                        perms,
                        null
                ).queue(this::restart);
            }

        } else if (entity instanceof Role && channel == null) {

            Role role = (Role) entity;

            if (isRevoke) {
                role.getManager().revokePermissions(perms).queue(this::restart);
            } else {
                role.getManager().givePermissions(perms).queue(this::restart);
            }

        } else if (entity instanceof Member && channel == null) {
            DiSky.exception(new DiSkyException("You cannot change permission of members without a specific channel!"), getNode());
        } else {

            Member member = (Member) entity;

            if (isRevoke) {
                channel.getManager().putMemberPermissionOverride(
                        member.getIdLong(),
                        null,
                        perms
                ).queue(this::restart);
            } else {
                channel.getManager().putMemberPermissionOverride(
                        member.getIdLong(),
                        perms,
                        null
                ).queue(this::restart);
            }

        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return (isRevoke ? "revoke " : "allow ") + "permissions " + exprPerms.toString(e, debug) +" to role / member " + exprTarget.toString(e, debug) + " using bot " + exprBot.toString(e, debug);
    }
}
