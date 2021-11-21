package info.itsthesky.disky3.core.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.ColorRGB;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.ReflectionUtils;
import info.itsthesky.disky3.api.Utils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@Name("Color from RGB / HEX")
@Description({"Get a skript color to use in embed, role, etc...",
"If SkImage is installed, both hex and rgb syntax will be prefixed by 'skript' to avoid confusion."})
public class ExprRGBColor extends SimpleExpression<Color> {

	static {
		final String prefix = ReflectionUtils.classExist("info.itsthesky.SkImage.SkImage") ? "skript " : "";
		Skript.registerExpression(ExprRGBColor.class, Color.class, ExpressionType.SIMPLE,
				"[the] "+prefix+"color from [the] (rgb|red green blue) %number%[ ][,][ ]%number%[ ][,][ ]%number%",
				"[the] "+prefix+ "color from [the] hex[adecimal] [input] %string%");
	}

	private Expression<Number> exprRed, exprGreen, exprBlue;
	private Expression<String> exprHex;
	private boolean isHex = false;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
		isHex = matchedPattern == 1;

		if (isHex) {
			exprHex = (Expression<String>) exprs[0];
		} else {
			exprRed = (Expression<Number>) exprs[0];
			exprGreen = (Expression<Number>) exprs[1];
			exprBlue = (Expression<Number>) exprs[2];
		}
		return true;
	}

	@Override
	protected Color @NotNull [] get(@NotNull Event e) {
		if (isHex) {
			final String input = exprHex.getSingle(e);
			if (input == null)
				return new Color[0];
			final java.awt.Color color = java.awt.Color.decode(input.contains("#") ? input : "#" + input);
			return new Color[] {new ColorRGB(color.getRed(), color.getGreen(), color.getBlue())};
		} else {
			int red = Utils.verifyVar(e, exprRed, 0).intValue();
			int green = Utils.verifyVar(e, exprGreen, 0).intValue();
			int blue = Utils.verifyVar(e, exprBlue, 0).intValue();
			return new Color[] {new ColorRGB(red, green, blue)};
		}
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public @NotNull Class<? extends Color> getReturnType() {
		return Color.class;
	}

	@Override
	public @NotNull String toString(Event e, boolean debug) {
		if (isHex) {
			return "color from hex " + exprHex.toString(e, debug);
		} else {
			return "color from rgb " + exprRed.toString(e, debug) + ", " + exprGreen.toString(e, debug) + ", " + exprBlue.toString(e, debug);
		}
	}

}