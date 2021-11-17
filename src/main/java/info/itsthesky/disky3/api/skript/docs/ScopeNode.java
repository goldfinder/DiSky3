package info.itsthesky.disky3.api.skript.docs;

import java.util.Locale;

public class ScopeNode {

    private final ScopeType type;
    private final String name;
    private final String desc;
    private final boolean require;

    public static ScopeNode fromText(String input) {
        final String[] values = input.split("\\|");
        final ScopeType type = ScopeType.valueOf(values[0].toUpperCase(Locale.ROOT));
        final String name = values[1];
        final String desc = values[2];
        final boolean require = Boolean.parseBoolean(values[3]);
        return new ScopeNode(type, name, desc, require);
    }

    private ScopeNode(ScopeType type, String name, String desc, boolean require) {
        this.type = type;
        this.name = name;
        this.desc = desc;
        this.require = require;
    }

    public static ScopeNode createEntry(String name, String desc, boolean require) {
        return new ScopeNode(ScopeType.ENTRY, name, desc, require);
    }

    public static ScopeNode createSection(String name, String desc, boolean require) {
        return new ScopeNode(ScopeType.SECTION, name, desc, require);
    }

    @Override
    public String toString() {
        return getType().name() + "|" + name + "|" + desc + "|" + isRequire();
    }

    public boolean isRequire() {
        return require;
    }

    public ScopeType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public enum ScopeType {
        ENTRY,
        SECTION,
        ;
    }

}
