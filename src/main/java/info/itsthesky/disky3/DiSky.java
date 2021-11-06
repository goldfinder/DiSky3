package info.itsthesky.disky3;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.util.Version;
import de.leonhard.storage.Json;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.ReflectionUtils;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.emojis.updated.EmojiStore;
import info.itsthesky.disky3.api.music.AudioUtils;
import info.itsthesky.disky3.api.skript.NodeInformation;
import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import info.itsthesky.disky3.api.skript.adapter.SkriptV2_3;
import info.itsthesky.disky3.api.skript.adapter.SkriptV2_6;
import info.itsthesky.disky3.api.updater.PluginUpdater;
import info.itsthesky.disky3.core.DiSkyCommand;
import info.itsthesky.disky3.core.EffChange;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public final class DiSky extends JavaPlugin {

    private static DiSky INSTANCE;
    private static SkriptAddon SKRIPT_ADDON;
    private static Version INSTALLED_SKRIPT_VERSION;
    private static SkriptAdapter SKRIPT_ADAPTER;
    private static Json DATA_CONTAINER;
    private static final boolean DEBUG = false;

    @Override
    public void onEnable() {

        INSTANCE = this;

        ln();
        warn("  _____  _  _____ _                     _____  _  __");
        warn(" |  __ \\(_)/ ____| |              /\\   |  __ \\| |/ /");
        warn(" | |  | |_| (___ | | ___   _     /  \\  | |__) | ' / ");
        warn(" | |  | | |\\___ \\| |/ / | | |   / /\\ \\ |  _  /|  <  ");
        warn(" | |__| | |____) |   <| |_| |  / ____ \\| | \\ \\| . \\ ");
        warn(" |_____/|_|_____/|_|\\_\\__,  | /_/    \\_\\_|  \\_\\_|\\_\\");
        warn("                        __/ |                       ");
        warn("                       |___/                        ");

        ln();
        // ################## SKRIPT ################## //
        if (ReflectionUtils.classExist("ch.njol.skript.Skript") && Skript.isAcceptRegistrations()) {

            success("Skript found! Starting registration ...");
            SKRIPT_ADDON = Skript.registerAddon(this);
            try {
                Skript.registerEffect(EffChange.class, EffChange.patterns.getPatterns());
                SKRIPT_ADDON.loadClasses("info.itsthesky.disky3.core");
            } catch (IOException e) {
                e.printStackTrace();
                error("Wait, this is anormal. Please report the error above on the DiSky GitHub!.");
                return;
            }
        } else {
            error("Unable to find Skript or Skript's registration is closed!");
            error("Can't load DiSky properly, disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        success("Loaded Skript's syntaxes successfully!");

        // This class is from 2.6-alpha1 and +
        final boolean use26 = ReflectionUtils.classExist("ch.njol.skript.conditions.CondIsPluginEnabled");
        SKRIPT_ADAPTER = use26 ? new SkriptV2_6() : new SkriptV2_3();

        if (SKRIPT_ADAPTER == null) {
            error("Unable to find a Skript adapter for your current Skript version ("+INSTALLED_SKRIPT_VERSION+")");
            error("DiSky could not work correctly, please install at least a 2.3.X Skript version!");
        } else {
            success("Successfully loaded Skript adapter for " + (use26 ? "2.6+" : "2.5-") + " version!");
        }

        log("Checking for updates ...");
        final PluginUpdater updater = PluginUpdater.create(this, "SkyCraft78", "DiSky3");
        final PluginUpdater.UpdateState state = updater.check();
        switch (state) {
            case LOWER:
                warn("You are using an outdated DiSky version!");
                warn("Latest is " + updater.getLatest() + ", but are are on " + getDescription().getVersion() + "!");
                warn("Update it now: https://github.com/SkyCraft78/DiSky3/releases/tag/" + updater.getLatest());
                break;
            case EQUAL:
                success("You are on the latest DiSky version! Well done!");
                break;
            case GREATER:
                warn("Detected a custom, tester or nighty DiSky version. Please report every bugs on DiSky's website!");
                break;
        }

        getCommand("disky").setExecutor(new DiSkyCommand());

        final File emojisFile = new File(getDataFolder(), "emojis.json");
        if (!emojisFile.exists()) {
            log("Saving emoji's file ...");
            try {
                InputStream stream = getResource("emojis.json");
                FileUtils.copyInputStreamToFile(stream, new File(getDataFolder(), "emojis.json"));
            } catch (IOException e) {
                e.printStackTrace();
                error("An error occurred while saving emojis file! Emojis will not be available.");
            }
            success("Success!");
        }
        log("Loading emoji library ...");
        try {
            EmojiStore.loadLocal();
        } catch (IOException e) {
            e.printStackTrace();
            error("An error occurred while loading emojis! They will not be available.");
        }
        success("Success!");

        try {
            InputStream stream = getResource("migrations/disky2to3.yml");
            FileUtils.copyInputStreamToFile(stream, new File(getDataFolder(), "migrations/disky2to3.yml"));
        } catch (IOException e) {
            e.printStackTrace();
            error("An error occurred while saving migration files.");
        }

        log("Starting audio module ...");
        AudioUtils.initializeAudio();
        success("Success!");

        log("Starting Data Container ...");
        DATA_CONTAINER = new Json(new File(getDataFolder(), "DATABASE.json"));
        success("Success!");

        ln();
        // ################## INFOS ################## //
        log("This is the first v3 of DiSky, including an awesome rework of DiSky!");
        log("If you want to support my work and my time on this addon, please consider donating!");
        log("PayPal Mail: itsthesky78@gmail.com");
        ln();
        success("DiSky seems to be loaded correctly!");
        success("If you found any bugs or have any suggestion, feel free to join our discord: https://discord.gg/whWuXwaVwM");
        ln();
    }

    @Override
    public void onDisable() {
        ln();
        log("Disabling DiSky's bot & instance ...");
        // Check https://github.com/DV8FromTheWorld/JDA/issues/1761#issuecomment-892921634
        try {
            BotManager.reset();
        } catch (Exception ex) {
            if (!(ex instanceof IllegalStateException))
                ex.printStackTrace();
        }
        success("Disable success!");
        ln();
        warn("Hope we see you back soon :<");
        ln();
        INSTANCE = null;
        SKRIPT_ADDON = null;
    }

    public static DiSky getInstance() {
        if (INSTANCE == null)
            throw new IllegalStateException("DiSky is not running!");
        return INSTANCE;
    }

    public static SkriptAdapter getSkriptAdapter() {
        return SKRIPT_ADAPTER;
    }

    public static SkriptAddon getSkriptAddon() {
        return SKRIPT_ADDON;
    }

    public static Json getDataContainer() {
        return DATA_CONTAINER;
    }

    public static void debug(String message) {
        if (DEBUG)
            log(message);
    }

    public static Version getInstalledSkriptVersion() {
        return INSTALLED_SKRIPT_VERSION;
    }

    public static void ln() {
        log("&1");
    }

    public static void log(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(Utils.colored("&3[DiSky] &b" + message));
    }

    public static void error(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(Utils.colored("&4[DiSky] &c" + message));
    }

    public static void success(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(Utils.colored("&2[DiSky] &a" + message));
    }

    public static void warn(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(Utils.colored("&6[DiSky] &e" + message));
    }

    public static void bigError(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(Utils.colored("&4&l#!#! &c" + message));
    }

    public static void lnError() {
        bigError("&1");
    }

    public static void exception(Throwable ex) {
        exception(ex, NodeInformation.last);
    }

    public static void exception(
            Throwable ex,
            @Nullable NodeInformation info
    ) {
        lnError();
        bigError("You just got an error while using DiSky!");
        bigError("Message: &4&n" + ex.getMessage());
        lnError();

        if (!(ex instanceof DiSkyException)) {
            bigError("If you don't understand the message just above, then send the following line to Sky on discord:");
            String skyRelatedLine = "Cannot determinate the related java line!";
            for (StackTraceElement e : ex.getStackTrace())
                if (e.toString().contains("itsthesky")) {
                    skyRelatedLine = e.toString();
                    break;
                }
            bigError(skyRelatedLine);
            lnError();
        }

        if (info != null ) {
            bigError("More informations:");
            bigError("   - Related script file: " + info.getFileName() + " (Line " + info.getLine() + ")");
            bigError("   - Related line code: " + info.getLineContent());
            lnError();
        }
        bigError("End of the error.");
        lnError();

    }
}
