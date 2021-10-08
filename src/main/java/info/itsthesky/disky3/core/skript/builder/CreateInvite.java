package info.itsthesky.disky3.core.skript.builder;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreateInvite extends WaiterEffect<Invite> {

    static {
        Skript.registerEffect(
                CreateInvite.class,
                "(make|create) [the] [new] invite in [the] [(guild|channel)] %guild/channel% [with max us(e|age)[s] %-number%] [with max (time|age) %-number%] [(with|using) [the] [bot] %-bot%] and store (it|the channel) in %object%"
        );
    }

    private Expression<Object> exprEntity;
    private Expression<Number> exprMaxUses;
    private Expression<Number> exprMaxAge;
    private Expression<Bot> exprBot;

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "create invite in  using bot " + exprBot.toString(e, debug);
    }

    @Override
    public boolean initEffect(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprEntity = (Expression<Object>) exprs[0];

        exprMaxUses = (Expression<Number>) exprs[1];
        exprMaxAge = (Expression<Number>) exprs[2];

        exprBot = Utils.verifyDefaultToEvent(exprs[3], exprBot, Bot.class);
        setChangedVariable((Variable<Invite>) exprs[4]);

        if (exprBot == null)
        {
            DiSky.exception(new DiSkyException("The bot cannot be retrieved in the create text channel effect!"), getNode());
            return false;
        }

        return true;
    }

    @Override
    public void runEffect(Event e) {
        Bot bot = exprBot.getSingle(e);
        Object entity = exprEntity.getSingle(e);

        Number maxUses = Utils.verifyVar(e, exprMaxUses, null);
        Number maxAge = Utils.verifyVar(e, exprMaxAge, null);

        if (entity == null || bot == null) return;

        final IInviteContainer channel;
        if (entity instanceof Guild) {
            Guild guild = bot.getCore().getGuildById(((Guild) entity).getId());
            channel = guild.getTextChannels().get(0);
        } else {
            channel = (IInviteContainer) bot.getCore().getGuildChannelById(((GuildChannel) entity).getId());
        }

        channel
                .createInvite()
                .setMaxUses(maxUses == null ? null : maxUses.intValue())
                .setMaxAge(maxAge == null ? null : maxAge.intValue())
                .queue(this::restart);

    }
}
