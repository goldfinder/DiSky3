package info.itsthesky.disky3.core.music.manage;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public abstract class AudioEffect extends Effect {

    private static final String END_PATTERN = " (in|from|of) [the] [guild] %guild% [(with|using) [bot] %-bot%]";

    public static void register(
            Class<? extends AudioEffect> eff,
            String pattern,
            @Nullable String after
    ) {
        pattern = pattern + END_PATTERN + (after == null ? "" : after);
        Skript.registerEffect(eff, pattern);
    }

    public static void register(
            Class<? extends AudioEffect> eff,
            String pattern
    ) {
        register(eff, pattern, null);
    }

    private Expression<Guild> exprGuild;
    private Expression<Bot> exprBot;

    @Override
    protected void execute(@NotNull Event e) {
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
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprGuild = (Expression<Guild>) exprs[0];
        exprBot = (Expression<Bot>) exprs[1];
        return true;
    }
}
