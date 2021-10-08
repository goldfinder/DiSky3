package info.itsthesky.disky3.api;

import info.itsthesky.disky3.DiSky;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public enum Configuration {

    PARSING_SEND_EFFECT(Boolean.class, false),
    ;

    private final Class clazz;
    private final Object defaultValue;

    <T> Configuration(Class<T> clazz, T defaultValue) {
        this.clazz = clazz;
        this.defaultValue = defaultValue;
    }

    static {
        Bukkit.getScheduler().runTaskLater(
                DiSky.getInstance(),
                () -> {
                    for (Configuration configuration : Configuration.values()) {
                        if (!configuration.getConfig().contains(configuration.getKey()))
                            configuration.getConfig().set(
                                    configuration.getKey(),
                                    configuration.getDefaultValue().toString()
                            );
                    }
                },
                50
        );
    }

    private FileConfiguration getConfig() {
        final File file = new File(DiSky.getInstance().getDataFolder(), "config.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!file.canRead())
            throw new IllegalStateException("Cannot create / load DiSky's configuration file.");
        return YamlConfiguration.loadConfiguration(file);
    }

    @SuppressWarnings("null")
    public <T> T get() {
        return getConfig().contains(getKey()) ? (T) getConfig().get(getKey()) : (T) defaultValue;
    }

    private String getKey() {
        final String str = name().replaceAll("_", " ");
        final String words[] = str.split("\\s");
        final StringBuilder capitalizeStr = new StringBuilder();
        for(String word : words){
            String firstLetter=word.substring(0,1);
            String remainingLetters=word.substring(1);
            capitalizeStr.append(firstLetter.toUpperCase()).append(remainingLetters).append(" ");
        }
        return capitalizeStr.toString().replaceAll(" ", ".");
    }

    public Class getClazz() {
        return clazz;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}
