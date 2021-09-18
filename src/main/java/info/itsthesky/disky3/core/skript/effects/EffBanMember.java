package info.itsthesky.disky3.core.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EffBanMember extends WaiterEffect {

    static {
        Skript.registerEffect(
                EffBanMember.class,
                "ban [the] [member] %member% [(due to|because of|with [the] reason) %-string%] [(remov|delet)ing %-number% day[s] of message[s]] [(with|using) [bot] %-bot%]"
        );
    }

    private Expression<Member> exprMember;
    private Expression<String> exprReason;
    private Expression<Number> exprDaysToRemove;
    private Expression<Bot> exprBot;

    @Override
    public void runEffect(@NotNull Event e) {
        Member member = exprMember.getSingle(e);
        final Bot bot = exprBot.getSingle(e);
        final Number days = Utils.verifyVar(e, exprDaysToRemove, 0);
        final @Nullable String reason = Utils.verifyVar(e, exprReason);

        if (member == null || bot == null) {
            restart();
            return;
        }

        member.getGuild().retrieveMemberById(member.getId()).queue(m -> {
            m.ban(days.intValue(), reason).queue(
                    (v) -> restart(),
                    ex -> DiSky.exception(ex, getNode())
            );
        });
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "ban " + exprMember.toString(e, debug) + (exprReason != null ? " because of " + exprReason.toString(e, debug) : "") + (exprDaysToRemove != null ? " removing " + exprDaysToRemove.toString(e, debug) + " message days" : "") + " using bot " +exprBot.toString(e, debug);
    }

    @Override
    public boolean initEffect(Expression @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprMember = (Expression<Member>) exprs[0];
        exprReason = (Expression<String>) exprs[1];
        exprDaysToRemove = (Expression<Number>) exprs[2];

        exprBot = (Expression<Bot>) exprs[3];
        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);
        if (exprBot == null)
        {
            Skript.error("Unable to get the bot in a ban effect.");
            return false;
        }

        return true;
    }
}
