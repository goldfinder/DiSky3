package info.itsthesky.disky3.core.music.manage;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AudioWaiterEffect<T> extends WaiterEffect<T> {

    private static final String END_PATTERN = " (in|from|of) [the] [guild] %guild% [(with|using) [bot] %-bot%]";

    public static void register(
            Class<? extends AudioWaiterEffect> eff,
            String pattern,
            String typeName
    ) {
        pattern = pattern + END_PATTERN + "[and store (it|the "+typeName+") in %-object%]";
        Skript.registerEffect(eff, pattern);
    }

    private Expression<Guild> exprGuild;
    private Expression<Bot> exprBot;

    @Override
    public void runEffect(Event e) {
        Guild guild = exprGuild.getSingle(e);
        final Bot bot = Utils.verifyVar(e, exprBot);
        if (guild == null)
            return;
        if (bot != null)
            guild = bot.getCore().getGuildById(guild.getIdLong());
        execute(guild);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return effectToString() + " in guild " + exprGuild.toString(e, debug) + (exprBot != null ? " with the bot " + exprBot.toString(e, debug) : "");
    }

    public abstract String effectToString();

    public abstract void execute(@NotNull Guild guild);

    @Override
    public boolean initEffect(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprGuild = (Expression<Guild>) exprs[0];
        exprBot = (Expression<Bot>) exprs[1];
        setChangedVariable((Variable<T>) exprs[2]);
        return true;
    }
}
