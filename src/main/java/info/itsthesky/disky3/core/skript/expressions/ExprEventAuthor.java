package info.itsthesky.disky3.core.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@Name("Event Author")
@Description("The user author in a log-related event, like the user who removed a role from another member.")
@Examples({"event log author",
"event-author"})
public class ExprEventAuthor extends SimpleExpression<User> {

	static {
		Skript.registerExpression(ExprEventAuthor.class, User.class, ExpressionType.SIMPLE,
				"event-author",
				"event [log] author");
	}

	@Override
	public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
		if (!(Arrays.asList(SkriptAdapter.getInstance().getCurrentEvents()[0].getInterfaces()).contains(LogEvent.class))) {
			Skript.error("Cannot use the event author in a no log-related event!");
			return false;
		}
		return true;
	}

	@Override
	protected User @NotNull [] get(final @NotNull Event e) {
		if (!(e instanceof LogEvent)) return new User[0];
		return new User[] {((LogEvent) e).getActionAuthor()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public @NotNull Class<? extends User> getReturnType() {
		return User.class;
	}

	@Override
	public @NotNull String toString(Event e, boolean debug) {
		return "event-author";
	}

}