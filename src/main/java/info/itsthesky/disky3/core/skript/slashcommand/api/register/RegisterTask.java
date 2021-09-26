package info.itsthesky.disky3.core.skript.slashcommand.api.register;

import ch.njol.util.Pair;
import info.itsthesky.disky3.core.skript.slashcommand.api.SlashObject;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RegisterTask extends BukkitRunnable {

    public static List<Pair<List<String>, SlashObject>> GUILDS_QUEUE = new ArrayList<>();
    public static List<Pair<List<String>, SlashObject>> BOTS_QUEUE = new ArrayList<>();
    public static boolean alreadyRan = false;

    @Override
    public void run() {
        alreadyRan = true;
        for (Pair<List<String>, SlashObject> pair : GUILDS_QUEUE)
            GuildRegister.registerCommand(pair.getFirst(), pair.getSecond());
        GUILDS_QUEUE = new ArrayList<>();

        for (Pair<List<String>, SlashObject> pair : BOTS_QUEUE)
            BotRegister.registerCommand(pair.getFirst(), pair.getSecond());
        BOTS_QUEUE = new ArrayList<>();
    }

}
