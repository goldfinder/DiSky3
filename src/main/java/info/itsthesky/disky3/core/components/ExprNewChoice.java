package info.itsthesky.disky3.core.components;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.emojis.Emote;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@Name("New Choice")
@Description("Create a new choice for dropdown with specified options. It can be either default options or not.")
public class ExprNewChoice extends SimpleExpression<SelectOption> {
    
    static {
        Skript.registerExpression(ExprNewChoice.class, SelectOption.class, ExpressionType.SIMPLE,
                "[a] new [default] choice with value %string% (with name|named) %string%[,] [with (desc|description) %-string%][,] [with [emoji] %-emote%]");
    }

    private NodeInformation information;
    private Expression<String> exprValue, exprName, exprDesc;
    private Expression<Emote> exprEmote;
    private boolean isDefault;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        information = new NodeInformation();
        exprValue = (Expression<String>) exprs[0];
        exprName = (Expression<String>) exprs[1];
        exprDesc = (Expression<String>) exprs[2];
        exprEmote = (Expression<Emote>) exprs[3];
        isDefault = parseResult.expr.contains("new default");
        return true;
    }

    @Override
    protected SelectOption @NotNull [] get(@NotNull Event e) {
        String value = exprValue.getSingle(e);
        String name = exprName.getSingle(e);
        String desc = Utils.verifyVar(e, exprDesc);
        Emote emote = Utils.verifyVar(e, exprEmote);
        if (value == null || name == null) return new SelectOption[0];

        if (desc != null && desc.length() > 100) {
            DiSky.exception(new DiSkyException("The choice description cannot be bigger than 100 characters!"), information);
            return new SelectOption[0];
        }

        SelectOption option;

        option = emote == null ? SelectOption.of(name, value).withDefault(isDefault) : SelectOption.of(name, value).withDefault(isDefault).withEmoji(Utils.convert(emote));
        if (desc != null)
            option = option.withDescription(desc);

        return new SelectOption[] {option};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends SelectOption> getReturnType() {
        return SelectOption.class;
    }

    @Override
    public @NotNull String toString(Event e, boolean debug) {
        return "new " + (isDefault ? "default " : "") + "choice with value " + exprValue.toString(e, debug) + " with name " + exprName.toString(e, debug) + (exprEmote == null ? "" : " with emote " + exprEmote.toString(e, debug));
    }
}
