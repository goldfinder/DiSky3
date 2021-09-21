package info.itsthesky.disky3.core.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EffPublishMessage extends WaiterEffect {

    static {
        Skript.registerEffect(
                EffPublishMessage.class,
                "(publish|crosspost) [the] [message] %message% [(with|using) [bot] %-bot%]"
        );
    }

    private Expression<UpdatingMessage> exprMessage;
    private Expression<Bot> exprBot;

    @Override
    public void runEffect(@NotNull Event e) {
        final UpdatingMessage message = exprMessage.getSingle(e);
        final Bot bot = exprBot.getSingle(e);
        if (message == null || bot == null)
            return;

        bot
                .getCore()
                .getTextChannelById(message.getMessage().getTextChannel().getId())
                .retrieveMessageById(message.getID())
                .queue(m -> {
                    if (!m.getTextChannel().isNews()) {
                        DiSky.exception(new DiSkyException("You can only publish a message in news channels!"), getNode());
                        restart();
                        return;
                    }
                    m.crosspost().queue(null, ex -> DiSky.exception(ex, getNode()));
                });
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "publish message " + exprMessage.toString(e, debug) + " using bot " +exprBot.toString(e, debug);
    }

    @Override
    public boolean initEffect(Expression @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprMessage = (Expression<UpdatingMessage>) exprs[0];

        exprBot = (Expression<Bot>) exprs[1];
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
