package info.itsthesky.disky3.api;

import de.leonhard.storage.Config;
import info.itsthesky.disky3.DiSky;

import java.io.File;

public enum Configuration {

    PARSING_SEND_EFFECT(Boolean.class, false, "ParsingSendEffect"),
    ;

    private final Class clazz;
    private final Object defaultValue;
    private final String key;

    <T> Configuration(Class<T> clazz, T defaultValue, String key) {
        this.clazz = clazz;
        this.defaultValue = defaultValue;
        this.key = key;
    }

    private Config getConfig() {
        return new Config(new File(DiSky.getInstance().getDataFolder(), "config.yml"));
    }

    @SuppressWarnings({"null", "unchecked"})
    public <T> T get() {
        return (T) getConfig().getOrSetDefault(getKey(), getDefaultValue());
    }

    private String getKey() {
        return this.key;
    }

    public Class getClazz() {
        return clazz;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}
