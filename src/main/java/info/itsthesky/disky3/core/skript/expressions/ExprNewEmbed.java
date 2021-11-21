package info.itsthesky.disky3.core.skript.expressions;

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
import info.itsthesky.disky3.api.EmbedManager;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@Name("New Embed")
@Description("Return an empty discord message embed.")
@Examples("set {_embed} to new discord embed")
@Since("3.0")
public class ExprNewEmbed extends SimpleExpression<EmbedBuilder> {

	static {
		Skript.registerExpression(ExprNewEmbed.class, EmbedBuilder.class, ExpressionType.SIMPLE,
				"new [discord] [message] embed [using [the] [template] [(named|with name|with id)] %-string%]"
		);
	}

	private Expression<String> exprTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
		if (exprs.length != 0) exprTemplate = (Expression<String>) exprs[0];
		return true;
	}

	@Override
	protected EmbedBuilder @NotNull [] get(@NotNull Event e) {
		if (exprTemplate == null) {
			return new EmbedBuilder[] {new EmbedBuilder()};
		} else {
			if (exprTemplate.getSingle(e) == null)
				return new EmbedBuilder[] {new EmbedBuilder()};
			return new EmbedBuilder[] {EmbedManager.getTemplate(exprTemplate.getSingle(e))};
		}
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public @NotNull Class<? extends EmbedBuilder> getReturnType() {
		return EmbedBuilder.class;
	}

	@Override
	public @NotNull String toString(Event e, boolean debug) {
		return "new discord embed";
	}

}