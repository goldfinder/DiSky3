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
import info.itsthesky.disky3.api.emojis.Emote;
import info.itsthesky.disky3.api.skript.events.MultipleWaiterEffect;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RetrieveEmotes extends MultipleWaiterEffect<Emote> {

    static {
        Skript.registerEffect(
                RetrieveEmotes.class,
                "retrieve [the] emotes (in|from|of) [the] [guild] %-guild% [with [the] [bot] %-bot%] and store (them|the emotes) in %objects%"
        );
    }

    private Expression<Bot> exprBot;
    private Expression<Guild> exprGuild;

    @Override
    public boolean initEffect(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprGuild = (Expression<Guild>) exprs[0];
        exprBot = (Expression<Bot>) exprs[1];
        Variable<Emote> var = Utils.parseVar((Expression<Emote>) exprs[2], true, true);
        if (var == null)
            return false;
        setChangedVariable(var);
        if (exprGuild == null)
            exprGuild = Utils.defaultToEventValue(exprGuild, Guild.class);

        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);

        if (exprBot == null || exprGuild == null) {
            DiSky.exception(new DiSkyException("The bot or the guild cannot be found in a retrieve emotes effect!"), getNode());
            return false;
        }

        return true;
    }

    @Override
    public void runEffect(Event e) {
        Bot bot = exprBot.getSingle(e);
        Guild guild = exprGuild.getSingle(e);
        if (bot == null || guild == null){
            restart();
            return;
        }

        bot
                .getCore()
                .getGuildById(guild.getId())
                .retrieveEmotes()
                .queue(emotes -> restart(emotes
                        .stream()
                        .map(Emote::fromJDA)
                        .toArray(Emote[]::new)));
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "retrieve emotes from guild " + exprGuild.toString(e, debug);
    }
}
