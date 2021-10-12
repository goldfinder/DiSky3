package info.itsthesky.disky3.core.music;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky3.api.music.AudioUtils;
import info.itsthesky.disky3.api.Utils;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@Name("Track the Bot is Playing")
@Description("Return the track (if set) which the bot is currently playing.")
@Since("1.6")
public class ExpTrackBotIsPlaying extends SimpleExpression<AudioTrack> {

	static {
		Skript.registerExpression(ExpTrackBotIsPlaying.class, AudioTrack.class, ExpressionType.SIMPLE,
				"[the] track [the] [bot] is playing in [the] [guild] %guild%");
	}

	Expression<Guild> exprGuild;

	@Override
	public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
		exprGuild = (Expression<Guild>) exprs[0];
		return true;
	}

	@Override
	protected AudioTrack @NotNull [] get(final @NotNull Event e) {
		Guild guild = exprGuild.getSingle(e);
		if (guild == null) return new AudioTrack[0];
		return !AudioUtils.MUSIC_MANAGERS.containsKey(guild.getIdLong()) ? new AudioTrack[0] : new AudioTrack[] {AudioUtils.MUSIC_MANAGERS.get(guild.getIdLong()).getPlayer().getPlayingTrack()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public @NotNull Class<? extends AudioTrack> getReturnType() {
		return AudioTrack.class;
	}

	@Override
	public @NotNull String toString(Event e, boolean debug) {
		return "track the bot is playing in guild " + exprGuild.toString(e, debug);
	}

}