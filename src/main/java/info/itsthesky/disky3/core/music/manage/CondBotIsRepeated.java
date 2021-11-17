package info.itsthesky.disky3.core.music.manage;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.music.AudioUtils;
import info.itsthesky.disky3.api.Utils;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@Name("Bot is Repeated in Guild")
@Description("Return if the bot is currently repeating the track in a specific guild.")
@Since("1.11")
public class CondBotIsRepeated extends Condition {

	static {
		Skript.registerCondition(CondBotIsRepeated.class,
				"[the] [bot] [%-bot%] is [currently] repeating [the track] in [the] [guild] %guild%",
				"[the] [bot] [%-bot%] (is not|isn't) [currently] repeating [the track] in [the] [guild] %guild%"
		);
	}

	Expression<Bot> exprBot;
	Expression<Guild> exprGuild;
	private boolean isNegate;

	@Override
	public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
		exprBot = (Expression<Bot>) exprs[0];
		exprGuild = (Expression<Guild>) exprs[1];
		isNegate = matchedPattern != 0;
		return true;
	}

	@Override
	public boolean check(final @NotNull Event e) {
		Guild guild = exprGuild.getSingle(e);
		final Bot bot = Utils.verifyVar(e, exprBot);
		if (guild == null) return false;
		if (bot != null)
			guild = bot.getCore().getGuildById(guild.getIdLong());
		if (isNegate) {
			return !AudioUtils.getGuildAudioPlayer(guild).getScheduler().isRepeated();
		} else {
			return AudioUtils.getGuildAudioPlayer(guild).getScheduler().isRepeated();
		}
	}

	@Override
	public @NotNull String toString(Event e, boolean debug) {
		return "track the bot is rpeating the track in guild " + exprGuild.toString(e, debug);
	}

}