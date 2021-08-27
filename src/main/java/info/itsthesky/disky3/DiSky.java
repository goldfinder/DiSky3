package info.itsthesky.disky3;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import info.itsthesky.disky3.api.Metrics;
import info.itsthesky.disky3.api.ReflectionUtils;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.BotManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class DiSky extends JavaPlugin {

    private static DiSky INSTANCE;
    private static SkriptAddon SKRIPT_ADDON;

    @Override
    public void onEnable() {

        log("");
        INSTANCE = this;

        ln();
        // ################## METRICS ################## //
        int pluginId = 10911;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SimplePie("skript_version", () ->
                Bukkit.getServer().getPluginManager().getPlugin("Skript").getDescription().getVersion()));
        metrics.addCustomChart(new Metrics.DrilldownPie("java_version", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            String javaVersion = System.getProperty("java.version");
            Map<String, Integer> entry = new HashMap<>();
            entry.put(javaVersion, 1);
            if (javaVersion.startsWith("1.7")) {
                map.put("Java 1.7", entry);
            } else if (javaVersion.startsWith("1.8")) {
                map.put("Java 1.8", entry);
            } else if (javaVersion.startsWith("1.9")) {
                map.put("Java 1.9", entry);
            } else {
                map.put("Other", entry);
            }
            return map;
        }));

        ln();
        // ################## SKRIPT ################## //
        if (ReflectionUtils.classExist("ch.njol.skript.Skript") && Skript.isAcceptRegistrations()) {

            success("Skript found! Starting registration ...");
            SKRIPT_ADDON = Skript.registerAddon(this);
            try {
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

        ln();
        // ################## INFOS ################## //
        log("This is the first v3 of DiSky, including an awesome rework of DiSky!");
        log("If you want to support my work and my time on this addon, please consider donating!");
        log("PayPal Mail: itsthesky78@gmail.com");
        ln();
        success("DiSky seems to be loaded correctly!");
        success("If you found any bugs or have any suggestion, feel free to join our discord: https://discord.gg/whWuXwaVwM");
        success("");
    }

    @Override
    public void onDisable() {
        BotManager.reset();
        INSTANCE = null;
    }

    public static DiSky getInstance() {
        if (INSTANCE == null)
            throw new IllegalStateException("DiSky is not running!");
        return INSTANCE;
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
        Bukkit.getServer().getConsoleSender().sendMessage(Utils.colored("&3[DiSky] &a" + message));
    }

    public static void warn(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(Utils.colored("&6[DiSky] &e" + message));
    }
}
