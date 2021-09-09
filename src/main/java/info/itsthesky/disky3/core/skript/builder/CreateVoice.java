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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreateVoice extends WaiterEffect<VoiceChannel> {

    static {
        Skript.registerEffect(
                CreateVoice.class,
                "(make|create) [the] [new] voice( |-)channel (named|with name) %string% in [the] [guild] %guild% [(with|using) [the] [bot] %-bot%] and store (it|the channel) in %object%"
        );
    }

    private Expression<String> exprName;
    private Expression<Guild> exprGuild;
    private Expression<Bot> exprBot;

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "create new text channel named " + exprName.toString(e, debug) + " using bot " + exprBot.toString(e, debug);
    }

    @Override
    public boolean initEffect(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprName = (Expression<String>) exprs[0];
        exprGuild = (Expression<Guild>) exprs[1];
        exprBot = Utils.verifyDefaultToEvent(exprs[2], exprBot, Bot.class);
        setChangedVariable((Variable<VoiceChannel>) exprs[3]);

        if (exprBot == null)
        {
            DiSky.exception(new DiSkyException("The bot cannot be retrieved in the create text channel effect!"), getNode());
            return false;
        }

        return true;
    }

    @Override
    public void runEffect(Event e) {
        String name = exprName.getSingle(e);
        Bot bot = exprBot.getSingle(e);
        Guild guild = exprGuild.getSingle(e);
        if (name == null || bot == null || guild == null) return;
        guild = bot.getCore().getGuildById(guild.getId());
        guild.createVoiceChannel(name).queue(this::restart);
    }
}
