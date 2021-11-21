package info.itsthesky.disky3.core.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import info.itsthesky.disky3.core.commands.CommandFactory;
import info.itsthesky.disky3.core.commands.CommandListener;
import info.itsthesky.disky3.core.commands.CommandObject;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Run Discord Command")
@Description({
        "Execute a registered discord command through your code.",
        "You will need to specify every event-values (the message only, DiSky will parse other entity from it) and, if there's, every require arguments to make it run the command.",
        "This will simulate a message receive event that the command will be based on, therefore use function for better performance!"
})
@Examples({
        "make bot \"name\" run discord command \"!test helloworld\" with message event-message",
        "# The line above will run the following discord command item:",
        "discord command test <text>:",
        "\tprefixes: !",
        "\ttrigger:",
        "\t\treply with arg-1"
})
public class EffRunCommand extends WaiterEffect {

    static {
        Skript.registerEffect(
                EffRunCommand.class,
                "make [the] [bot] %bot% (execute|run) [the] [discord] command %string% with message %message%"
        );
    }

    private Expression<Bot> exprBot;
    private Expression<String> exprCmd;
    private Expression<UpdatingMessage> exprMessage;
    private @Nullable Expression<Object> exprArgs;

    @Override
    public boolean initEffect(Expression[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprBot = (Expression<Bot>) expressions[0];
        exprCmd = (Expression<String>) expressions[1];
        exprMessage = (Expression<UpdatingMessage>) expressions[2];
        return true;
    }

    @Override
    public void runEffect(Event e) {
        final String cmd = exprCmd.getSingle(e);
        final Bot bot = exprBot.getSingle(e);
        final UpdatingMessage message = exprMessage.getSingle(e);
        final @Nullable Object[] args = Utils.verifyVars(e, exprArgs, null);
        if (Utils.isAnyNull(cmd, bot, message)) {
            restart();
            return;
        }

        final MessageReceivedEvent event = new MessageReceivedEvent(bot.getCore(), 200, message.getMessage());
        CommandListener.exe(event, cmd);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "make bot " + exprBot.toString(e, debug) + " run discord command " + exprCmd.toString(e, debug) + " with message " + exprMessage.toString(e, debug);
    }
}
