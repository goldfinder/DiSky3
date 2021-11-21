package info.itsthesky.disky3.core.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@Name("Bot Named")
@Description("Get a bot object from its name. If it was not loaded, DiSky will throw a console exception and will return null.")
@Examples("bot named \"hello\"")
public class ExprBot extends SimpleExpression<Bot> {

	static {
		Skript.registerExpression(ExprBot.class, Bot.class, ExpressionType.SIMPLE,
				"[the] bot [(with name|named)] %string%");
	}

	private Expression<String> exprInput;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
		exprInput = (Expression<String>) exprs[0];
		return true;
	}

	@Override
	protected Bot @NotNull [] get(@NotNull Event e) {
		String input = Utils.verifyVar(e, exprInput);
		if (input == null)
			return new Bot[0];
		return new Bot[] {BotManager.searchFromName(input)};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public @NotNull Class<? extends Bot> getReturnType() {
		return Bot.class;
	}

	@Override
	public @NotNull String toString(Event e, boolean debug) {
		return "bot with name " + exprInput.toString(e, debug);
	}

}