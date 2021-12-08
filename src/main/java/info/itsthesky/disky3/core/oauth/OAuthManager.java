package info.itsthesky.disky3.core.oauth;

import de.leonhard.storage.Yaml;
import de.leonhard.storage.util.FileUtils;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotApplication;
import info.itsthesky.disky3.api.section.EffectSection;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Some utils method to handle OAuth2
 */
public final class OAuthManager {

    private static final HashMap<OAuthWrapper, OAuthServer> SERVERS = new HashMap<>();

    public static boolean checkBot(@Nullable Bot bot) {
        return bot != null && bot.hasApplication();
    }

    public static void registerWebserver(OAuthWrapper wrapper, EffectSection section) {
        SERVERS.put(wrapper, new OAuthServer(wrapper, section));
    }

    public static void shutdown() {
        SERVERS.values().forEach(OAuthServer::shutdown);
    }

    // ##### CONFIGURATION

    public static Yaml getConfig() {
        return new Yaml(new File(DiSky.getInstance().getDataFolder(), "oauth.yml"));
    }

    public static int getPort() {
        return getConfig().getOrSetDefault("Port", 3650);
    }

    public static void checkFile(File file, String defaultValue) {
        if (!file.exists())
        {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            FileUtils.write(file, Collections.singletonList(defaultValue));
        }
    }

    public static String getErrorCode() {
        final File file = new File(DiSky.getInstance().getDataFolder(), "error.html");
        checkFile(file, "<h1>Invalid response code!</h1>");
        return String.join("\n", FileUtils.readAllLines(file));
    }

    public static String getValidCode() {
        final File file = new File(DiSky.getInstance().getDataFolder(), "valid.html");
        checkFile(file, "<h1>Login success!</h1>\n<h2>You can go back to discord.</h2>");
        return String.join("\n", FileUtils.readAllLines(file));
    }

}
