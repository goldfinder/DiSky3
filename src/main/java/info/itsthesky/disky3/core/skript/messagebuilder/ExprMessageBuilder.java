package info.itsthesky.disky3.core.skript.messagebuilder;

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
import net.dv8tion.jda.api.MessageBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public class ExprMessageBuilder extends SimpleExpression<MessageBuilder> {

	static {
		Skript.registerExpression(ExprMessageBuilder.class, MessageBuilder.class, ExpressionType.SIMPLE,
				"[a] new [discord] message builder"
		);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
		return true;
	}

	@Override
	protected MessageBuilder @NotNull [] get(@NotNull Event e) {
		return new MessageBuilder[] {
				new MessageBuilder()
		};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public @NotNull Class<? extends MessageBuilder> getReturnType() {
		return MessageBuilder.class;
	}

	@Override
	public @NotNull String toString(Event e, boolean debug) {
		return "new message builder";
	}

}