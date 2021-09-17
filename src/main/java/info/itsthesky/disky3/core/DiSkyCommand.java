package info.itsthesky.disky3.core;

import ch.njol.skript.Skript;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static info.itsthesky.disky3.DiSky.*;

public class DiSkyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage("This command can only be executed in console!");
            return false;
        }

        if (args.length == 0) {
            sendHelp();
            return true;
        }

        switch (args[0]) {
            case "help":
                sendHelp();
                return true;
            case "list":
                ln();

                if (BotManager.getLoadedBots().size() == 0) {
                    warn("There's no loaded bot :(");
                } else {
                    warn("Loaded bots ("+BotManager.getLoadedBots().size()+"):");
                    ln();
                    for (Bot bot : BotManager.getLoadedBots())
                        warn("    - " + bot.getName() + " (&6Connected as "+bot.getCore().getSelfUser().getName() + "#" +bot.getCore().getSelfUser().getDiscriminator()+"&e)");
                }

                ln();
                return true;
            case "debug":
                ln();
                log("Debug of " + Utils.now() + ":");
                ln();
                log("    DiSky version: &3v" + DiSky.getInstance().getDescription().getVersion());
                log("    Skript version: &3v" + Skript.getVersion());
                log("    Server Version: &3v" + Bukkit.getVersion() + "");
                log("    Loaded Bots: &3" + BotManager.getLoadedBots().size() + " bot(s)");
                log("    JDA Version: &3" + JDAInfo.VERSION + " bot(s)");
                ln();
                return true;
            case "info":

                if (args.length == 1) {
                    error("You must specify a bot name! Use &4/disky list &cto show every loaded bots!");
                    return false;
                }
                final String botName = args[1];
                if (!BotManager.isLoaded(botName)) {
                    error("The specified bot name doesn't exist (or is not loaded)! Use &4/disky list &cto show every loaded bots!");
                    return false;
                }
                final Bot bot = BotManager.searchFromName(botName);

                ln();
                log("Bot information about '" + bot.getName() + "':");
                ln();
                log("    - Discord Name & Tag: &3" + bot.getCore().getSelfUser().getName() + "#" +bot.getCore().getSelfUser().getDiscriminator());
                log("    - Discord ID: &3" + bot.getCore().getSelfUser().getId());
                log("    - Total Guilds: &3" + bot.getCore().getGuilds().size());
                log("    - Gateway Ping: &3" + bot.getCore().getGatewayPing());
                log("    - Internal Status: &3" + bot.getCore().getStatus().name().toLowerCase(Locale.ROOT).replaceAll("_", " "));
                log("    - Intents:");
                for (GatewayIntent intent : GatewayIntent.values())
                    log("    " + (
                            bot.getCore().getGatewayIntents().contains(intent) ? "&a✓ " : "&c☓ "
                            ) + intent.name().toLowerCase(Locale.ROOT).replaceAll("_", " ")
                    );
                ln();

                return true;
        }

        sendHelp();
        return true;
    }

    public static void sendHelp() {
        ln();
        success("DiSky v" + DiSky.getInstance().getDescription().getVersion() + " by ItsTheSky - Help List");
        ln();
        log("/disky help &7- &3Show this page");
        log("/disky info <bot name> &7- &3Show information about a loaded bot");
        log("/disky list &7- &3Show a list of every loaded bot");
        log("/disky debug &7- &3Show debug information (to send in our Discord for help)");
        ln();
    }
}
