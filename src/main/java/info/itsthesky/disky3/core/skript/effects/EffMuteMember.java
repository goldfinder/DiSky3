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

public class EffMuteMember extends WaiterEffect {

    static {
        Skript.registerEffect(
                EffMuteMember.class,
                "mute [the] [member] %member% [(with|using) [bot] %-bot%]",
                "unmute [the] [member] %member% [(with|using) [bot] %-bot%]"
        );
    }

    private Expression<Member> exprMember;
    private Expression<Bot> exprBot;
    private boolean isUnMute;

    @Override
    public void runEffect(@NotNull Event e) {
        Member member = exprMember.getSingle(e);
        final Bot bot = exprBot.getSingle(e);

        if (member == null || bot == null) {
            restart();
            return;
        }

        member.getGuild().retrieveMemberById(member.getId()).queue(m -> {
            m.mute(!isUnMute).queue(this::restart);
        });
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return (isUnMute ? "unmute" : "mute") + " " + exprMember.toString(e, debug) + " using bot " +exprBot.toString(e, debug);
    }

    @Override
    public boolean initEffect(Expression @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprMember = (Expression<Member>) exprs[0];

        exprBot = (Expression<Bot>) exprs[1];
        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);
        if (exprBot == null)
        {
            Skript.error("Unable to get the bot in a mute member effect.");
            return false;
        }

        isUnMute = matchedPattern == 1;
        return true;
    }
}
