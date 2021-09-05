package info.itsthesky.disky3.core.skript.messagebuilder;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import org.bukkit.event.Event;

public class EffAppend extends Effect {

    static {
        Skript.registerEffect(EffAppend.class,
                "append %string/embedbuilder% to [message] [builder] %messagebuilder%");
    }

    private Expression<Object> exprEntity;
    private Expression<MessageBuilder> exprBuilder;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprEntity = (Expression<Object>) exprs[0];
        exprBuilder = (Expression<MessageBuilder>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        Object entity = exprEntity.getSingle(e);
        MessageBuilder builder = exprBuilder.getSingle(e);
        if (entity == null || builder == null) return;
        if (entity instanceof EmbedBuilder) {
            builder.setEmbeds(
                    ((EmbedBuilder) entity).build()
            );
        } else {
            builder.append(entity.toString());
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "append " + exprEntity.toString(e, debug) + " to message builder " + exprBuilder.toString(e, debug);
    }

}