package info.itsthesky.disky3.core.skript.messagebuilder;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import net.dv8tion.jda.api.MessageBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ExprBuilderTTS extends SimplePropertyExpression<MessageBuilder, Boolean> {

    static {
        register(ExprBuilderTTS.class, Boolean.class,
                "[discord] tts [state]",
                "messagebuilder"
        );
    }

    @Override
    public @NotNull Boolean convert(MessageBuilder entity) {
        return entity.build().isTTS();
    }

    @Override
    public @NotNull Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "tts state";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode.equals(Changer.ChangeMode.SET)) {
            return CollectionUtils.array(Boolean.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(@NotNull Event e, @Nullable Object[] delta, Changer.@NotNull ChangeMode mode) {
        if (delta == null || delta[0] == null || !(delta[0] instanceof Boolean) || !(mode.equals(Changer.ChangeMode.SET))) return;
        boolean value = (Boolean) delta[0];
        for (MessageBuilder builder : getExpr().getAll(e)) builder.setTTS(value);
    }
}