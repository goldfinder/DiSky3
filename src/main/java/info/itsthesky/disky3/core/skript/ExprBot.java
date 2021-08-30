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

@Name("Bot Named")
@Description("Get a bot object from its name.")
@Examples("bot named \"hello\"")
@Since("3.0")
public class ExprBot extends SimpleExpression<Bot> {

	static {
		Skript.registerExpression(ExprBot.class, Bot.class, ExpressionType.SIMPLE,
				"[the] bot [(with name|named)] %string%");
	}

	private Expression<String> exprInput;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprInput = (Expression<String>) exprs[0];
		return true;
	}

	@Override
	protected Bot[] get(Event e) {
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
	public Class<? extends Bot> getReturnType() {
		return Bot.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "bot with name " + exprInput.toString(e, debug);
	}

}