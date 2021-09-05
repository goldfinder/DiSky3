package info.itsthesky.disky3.core.skript.getter;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RetrieveMessage extends WaiterEffect<UpdatingMessage> {

    static {
        Skript.registerEffect(
                RetrieveMessage.class,
                "retrieve [the] message (from|with) id %string% [in [the] [text]( |-)channel %-textchannel%] [with [the] [bot] %-bot%] and store (it|the user) in %object%"
        );
    }

    private Expression<String> exprID;
    private Expression<Bot> exprBot;
    private Expression<TextChannel> exprChannel;

    @Override
    public boolean initEffect(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprID = (Expression<String>) exprs[0];
        exprChannel = (Expression<TextChannel>) exprs[1];
        exprBot = (Expression<Bot>) exprs[2];
        setChangedVariable((Variable<UpdatingMessage>) exprs[3]);

        if (exprChannel == null)
            exprChannel = Utils.defaultToEventValue(exprChannel, TextChannel.class);

        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);

        if (exprBot == null || exprChannel == null) {
            DiSky.exception(new DiSkyException("The bot or the channel cannot be found in a retrieve message effect!"), getNode());
            return false;
        }

        return true;
    }

    @Override
    public void runEffect(Event e) {
        Bot bot = exprBot.getSingle(e);
        String id = exprID.getSingle(e);
        TextChannel channel = exprChannel.getSingle(e);
        if (id == null || bot == null || channel == null) return;

        bot
                .getCore()
                .getGuildById(channel.getGuild().getId())
                .getTextChannelById(channel.getId())
                .retrieveMessageById(id)
                .queue(msg -> restart(UpdatingMessage.from(msg)), ex -> DiSky.exception(ex, getNode()));
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "retrieve message with id " + exprID.toString(e, debug);
    }
}
