package info.itsthesky.disky3;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.config.Node;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.util.Version;
import info.itsthesky.disky3.api.*;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.NodeInformation;
import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import info.itsthesky.disky3.api.skript.adapter.SkriptV2_3;
import info.itsthesky.disky3.api.skript.adapter.SkriptV2_6;
import info.itsthesky.disky3.core.DiSkyCommand;
import info.itsthesky.disky3.core.EffChange;
import info.itsthesky.disky3.core.skript.errorhandler.SectionTry;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public final class DiSky extends JavaPlugin {

    private static DiSky INSTANCE;
    private static SkriptAddon SKRIPT_ADDON;
    private static Version INSTALLED_SKRIPT_VERSION;
    private static SkriptAdapter SKRIPT_ADAPTER;
    private static final boolean DEBUG = true;

    @Override
    public void onEnable() {

        INSTANCE = this;

        // ################## METRICS ################## //
        int pluginId = 10911;
        Metrics metrics = new Metrics(this, pluginId);

        ln();
        warn("  _____  _  _____ _                 ____  ");
        warn(" |  __ \\(_)/ ____| |               |___ \\ ");
        warn(" | |  | |_| (___ | | ___   _  __   ____) |");
        warn(" | |  | | |\\___ \\| |/ / | | | \\ \\ / /__ < ");
        warn(" | |__| | |____) |   <| |_| |  \\ V /___) |");
        warn(" |_____/|_|_____/|_|\\_\\__, |   \\_/|____/ ");
        warn("                        __/ |             ");
        warn("                       |___/              ");

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

        INSTALLED_SKRIPT_VERSION = new Version("" + Skript.getInstance().getDescription().getVersion()); // Skript version;
        for (SkriptAdapter adapter : new SkriptAdapter[] {new SkriptV2_3(), new SkriptV2_6()}) {
            if (INSTALLED_SKRIPT_VERSION.isSmallerThan(adapter.getMinimalVersion()))
                continue;
            SKRIPT_ADAPTER = adapter;
        }

        if (SKRIPT_ADAPTER == null) {
            error("Unable to find a Skript adapter for your current Skript version ("+INSTALLED_SKRIPT_VERSION+")");
            error("DiSky could not work correctly, please install at least a 2.3.X Skript version!");
        } else {
            success("Successfully loaded Skript adapter for minimal " + SKRIPT_ADAPTER.getMinimalVersion() + " version!");
        }

        getCommand("disky").setExecutor(new DiSkyCommand());

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

        throw new DiSkyRuntimeException(ex);

    }
}
