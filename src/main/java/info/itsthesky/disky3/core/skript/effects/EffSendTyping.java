package info.itsthesky.disky3.core.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.entities.BaseGuildMessageChannel;
import net.dv8tion.jda.api.entities.GuildChannel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EffSendTyping extends WaiterEffect {

    static {
        Skript.registerEffect(
                EffSendTyping.class,
                "send typing in [the] [channel] %channel% [(with|using) [the] [bot] %-bot%]"
        );
    }

    private Expression<Bot> exprBot;
    private Expression<GuildChannel> exprChannel;

    @Override
    public void runEffect(@NotNull Event e) {
        GuildChannel channel = exprChannel.getSingle(e);
        final @Nullable Bot bot = Utils.verifyVar(e, exprBot);
        if (channel == null) {
            restart();
            return;
        }
        if (bot != null)
            channel = bot.getCore().getGuildChannelById(channel.getId());
        ((BaseGuildMessageChannel) channel).sendTyping().queue(this::restart);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "send typing in " + exprChannel.toString(e, debug) + " using bot " + exprBot.toString(e, debug);
    }

    @Override
    public boolean initEffect(Expression @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprChannel = (Expression<GuildChannel>) exprs[0];
        exprBot = (Expression<Bot>) exprs[1];
        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);
        return true;
    }
}
