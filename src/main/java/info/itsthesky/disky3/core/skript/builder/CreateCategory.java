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
import net.dv8tion.jda.api.entities.Category;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreateCategory extends WaiterEffect<Category> {

    static {
        Skript.registerEffect(
                CreateCategory.class,
                "(make|create) [the] [new] category (named|with name) %string% in [the] [guild] %guild% [(with|using) [the] [bot] %-bot%] and store (it|the category) in %object%"
        );
    }

    private Expression<String> exprName;
    private Expression<Guild> exprGuild;
    private Expression<Bot> exprBot;

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "create new category named " + exprName.toString(e, debug) + " using bot " + exprBot.toString(e, debug);
    }

    @Override
    public boolean initEffect(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprName = (Expression<String>) exprs[0];
        exprGuild = (Expression<Guild>) exprs[1];
        exprBot = Utils.verifyDefaultToEvent(exprs[2], exprBot, Bot.class);
        setChangedVariable((Variable<Category>) exprs[3]);

        if (exprBot == null)
        {
            DiSky.exception(new DiSkyException("The bot cannot be retrieved in the create category effect!"), getNode());
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
        guild.createCategory(name).queue(this::restart);
    }
}
