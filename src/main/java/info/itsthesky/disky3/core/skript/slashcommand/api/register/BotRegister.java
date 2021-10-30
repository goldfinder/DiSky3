package info.itsthesky.disky3.core.skript.slashcommand.api.register;

import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.core.skript.slashcommand.api.SlashObject;
import info.itsthesky.disky3.core.skript.slashcommand.api.SlashUtils;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.bukkit.Bukkit;

import java.util.List;

public final class BotRegister extends RegisterDB {

    private static final BotRegister instance;

    static {
        instance = new BotRegister();
    }

    public BotRegister() {
        super("bots");
    }

    public static BotRegister getInstance() {
        return instance;
    }

    public void registerCommand(List<String> bots, SlashObject command) {
        command.setGlobalRegister(false);
        Bukkit.getScheduler().runTaskLater(
                DiSky.getInstance(),
                () -> registerCommand(command, SlashUtils.parseBots(bots)),
                30
        );
    }

    public void registerCommand(SlashObject command, List<Bot> bots) {
        CommandData data = SlashUtils.parseCommand(command);
        if (data == null) return;

        for (Bot bot : bots) {
            bot.getCore().retrieveCommands().queue(cmds -> {
                for (Command loadedCommand : cmds) {
                    if (!loadedCommand.getName().equalsIgnoreCase(data.getName()))
                        return;
                    // Mean they are equal, so we can just edit them
                    if (alreadyRegistered(bot, data)) {

                        DiSky.debug("Registering command " + command.getName() + " using edit way.");
                        editCommand(data, loadedCommand).queue();
                        return;

                    }
                }
                // We have to create them

                DiSky.debug("Registering command " + command.getName() + " using creation way.");
                createCommand(bot, data).queue();
            });
        }
    }

    public void unregister(List<String> bots, SlashObject cmd) {
        for (Bot bot : SlashUtils.parseBots(bots)) {
            bot.getCore().retrieveCommands().queue(
                    cmds -> cmds.forEach(cmd2 -> {
                        if (cmd2.getName().equalsIgnoreCase(cmd.getName()))
                            cmd2.delete().queue();
                    })
            );
        }
    }

    public CommandListUpdateAction createCommand(Bot bot, CommandData data) {
        addCommand(bot, data,true);
        return bot
                .getCore()
                .updateCommands()
                .addCommands(getCommands(bot).values());
    }

}
