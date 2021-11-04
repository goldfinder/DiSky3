package info.itsthesky.disky3.core.skript.slashcommand;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Utils;
import ch.njol.util.Kleenean;
import ch.njol.util.StringUtils;
import info.itsthesky.disky3.core.skript.slashcommand.api.SlashArgument;
import info.itsthesky.disky3.core.skript.slashcommand.api.SlashUtils;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Peter Güttinger
 * edited minorly for DiSky by sky for slash command
 */
@Name("Argument")
@Description({"Represent an argument inside a discord command section.",
"The argument will always parse according to the given type, so no argument can be interpreted as object.",
"As same as the skript command's argument, when you have multiple present you need to count the such as:",
"arg-1, argument 2, etc..."})
public class ExprArgument extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprArgument.class, Object.class, ExpressionType.SIMPLE,
                "[][the] <(\\d*1)st|(\\d*2)nd|(\\d*3)rd|(\\d*[4-90])th> arg[ument][s]",
                "[][the] arg[ument][s]",
                "[][the] arg[ument][s](-| )<(\\d+)>"
        );
    }

    @SuppressWarnings("null")
    private int optionIndex;
    private SlashArgument arg;

    @Override
    public boolean init(final Expression<?> @NotNull [] exprs, final int matchedPattern, final @NotNull Kleenean isDelayed, final @NotNull ParseResult parser) {
        if (!ScriptLoader.isCurrentEvent(SlashEvent.class))
            return false;

        List<SlashArgument> currentArguments = SlashFactory.getInstance().currentArguments;
        SlashArgument arg;

        if (currentArguments.size() == 0) {
            Skript.error("This command doesn't have any arguments", ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        int index;
        switch (matchedPattern) {
            case 2:
            case 0:
                index = Utils.parseInt(parser.regexes.get(0).group(1));
                if (index > currentArguments.size()) {
                    Skript.error("The command doesn't have a " + StringUtils.fancyOrderNumber(index) + " argument", ErrorQuality.SEMANTIC_ERROR);
                    return false;
                }
                arg = currentArguments.get(index - 1);
                break;
            case 1:
                if (currentArguments.size() == 1) {
                    index = 0;
                } else {
                    Skript.error("'argument(s)' cannot be used if the command has multiple arguments. Use 'argument 1', 'argument 2', etc. instead", ErrorQuality.SEMANTIC_ERROR);
                    return false;
                }
                arg = currentArguments.get(index - 1);
                break;
            default:
                assert false : matchedPattern;
                return false;
        }
        this.optionIndex = index - 1;
        this.arg = arg;
        return true;
    }

    @Override
    protected Object @NotNull [] get(final @NotNull Event e) {
        SlashEvent event = (SlashEvent) e;
        int index = this.optionIndex;
        OptionMapping option;
         try {
             option = event.getJDAEvent().getOptions().get(index);
         } catch (Exception ex) {
             return new Object[0];
         }
        return new Object[] {SlashUtils.convert(option)};
    }

    @Override
    public @NotNull Class<? extends Object> getReturnType() {
        return SlashUtils.convert(arg.getType());
    }

    @Override
    public @NotNull String toString(final @Nullable Event e, final boolean debug) {
        if (e == null)
            return "the " + StringUtils.fancyOrderNumber(optionIndex + 1) + " argument";
        return Classes.getDebugMessage(getArray(e));
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public boolean isLoopOf(final @NotNull String s) {
        return false;
    }

}