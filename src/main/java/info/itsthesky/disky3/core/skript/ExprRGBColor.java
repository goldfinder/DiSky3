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
import ch.njol.skript.util.Color;
import ch.njol.skript.util.ColorRGB;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import org.bukkit.event.Event;

public class ExprRGBColor extends SimpleExpression<Color> {

	static {
		Skript.registerExpression(ExprRGBColor.class, Color.class, ExpressionType.SIMPLE,
				"[the] color from [the] (rgb|red green blue) %number%[ ][,][ ]%number%[ ][,][ ]%number%");
	}

	private Expression<Number> exprRed, exprGreen, exprBlue;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprRed = (Expression<Number>) exprs[0];
		exprGreen = (Expression<Number>) exprs[1];
		exprBlue = (Expression<Number>) exprs[2];
		return true;
	}

	@Override
	protected Color[] get(Event e) {
		int red = Utils.verifyVar(e, exprRed, 0).intValue();
		int green = Utils.verifyVar(e, exprGreen, 0).intValue();
		int blue = Utils.verifyVar(e, exprBlue, 0).intValue();
		return new Color[] {new ColorRGB(red, green, blue)};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Color> getReturnType() {
		return Color.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "color from rgb " + exprRed.toString(e, debug) + " " + exprGreen.toString(e, debug) + " " + exprBlue.toString(e, debug);
	}

}