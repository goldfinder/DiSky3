package info.itsthesky.disky3.core.skript.getter;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotChangers;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseRetrieveEffect<T, E> extends WaiterEffect<T> {

    public static void register(Class<? extends WaiterEffect<?>> clazz,
                                String entityName,
                                String sourceInfo) {
        register(clazz, entityName, sourceInfo, true, true);
    }

    public static void register(Class<? extends WaiterEffect<?>> clazz,
                                String entityName,
                                String sourceInfo,
                                boolean requireID,
                                boolean allowCustomBot) {
        Skript.registerEffect(
                clazz,
                "retrieve " + entityName + (requireID ? " (with|from) id %string% " : " ") + "(from|with|of|in) %"+sourceInfo+"%" + (allowCustomBot ? " [(with|using) [the] [bot] %-bot%]" : "") + " and store (it|the "+entityName+") in %-object%"
        );
    }

    private Expression<String> exprInput;
    private Expression<E> exprEntity;
    private Expression<Bot> exprBot;

    @Override
    @SuppressWarnings("unchecked")
    public boolean initEffect(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        if (requireID()) {
            exprInput = (Expression<String>) expressions[0];
            exprEntity = (Expression<E>) expressions[1];
        } else {
            exprEntity = (Expression<E>) expressions[0];
        }
        if (allowCustomBot())
            exprBot = (Expression<Bot>) expressions[ requireID() ? 2 : 1 ];

        final int varIndex = requireID() ? 3 : 2;
        return validateVariable(expressions[ allowCustomBot() ? varIndex : varIndex-1 ], false);
    }

    protected boolean requireID() {
        return true;
    };

    protected boolean allowCustomBot() {
        return true;
    };

    protected abstract RestAction<T> retrieve(@NotNull String input, @NotNull E entity);

    @Override
    public void runEffect(Event e) {
        final @Nullable String input = Utils.verifyVar(e, exprInput);
        final @Nullable Bot bot = Utils.verifyVar(e, exprBot);
        E entity = exprEntity.getSingle(e);

        if (entity == null) {
            restart();
            return;
        }

        if (bot != null)
            entity = BotChangers.convert(entity, bot);

        if (requireID() && input == null) {
            restart();
            return;
        }

        try {
            final RestAction<T> action = retrieve(input, entity);
            action.queue(this::restart, ex -> DiSky.exception(ex, getNode()));
        } catch (Exception ex) {
            DiSky.exception(ex, getNode());
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        final String idPattern = requireID() ? "with id " + exprInput.toString(e, debug) : "";
        return "retrieve entity "+idPattern+"from entity " + exprEntity.toString(e, debug) + " with bot " + exprBot.toString(e, debug) + " and store it in " + getChangedVariable().toString(e, debug);
    }
}
