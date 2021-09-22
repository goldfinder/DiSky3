package info.itsthesky.disky3.core.skript.slashcommand;

import ch.njol.util.NonNullPair;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.internal.utils.Checks;
import net.dv8tion.jda.internal.utils.tuple.MutableTriple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlashArgument {

    private final OptionType type;
    private final boolean optional;
    private String desc;
    private String name;
    private List<SlashPreset> presets;

    public SlashArgument(
            OptionType type,
            boolean optional
    ) {
        this(type, optional, "Default argument description", type.name().toLowerCase(Locale.ROOT).replaceAll("_", " "), new ArrayList<>());
    }

    public SlashArgument(
            OptionType type,
            boolean optional,
            String desc,
            String name,
            List<SlashPreset> presets
    ) {
        this.optional = optional;
        this.type = type;
        this.desc = desc;
        this.name = name;
        this.presets = presets;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPresets(List<SlashPreset> presets) {
        this.presets = presets;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public List<SlashPreset> getPresets() {
        return presets;
    }

    public OptionType getType() {
        return type;
    }

    public boolean isOptional() {
        return optional;
    }

    public static class SlashPreset {

        private String name;
        private Object value;
        private final boolean shouldUseInt;

        public SlashPreset(
                String name,
                Object value,
                SlashArgument instance
        ) {
            shouldUseInt = instance.getType().equals(OptionType.INTEGER);
            if (
                    !instance.getType().equals(OptionType.STRING) &&
                            !instance.getType().equals(OptionType.INTEGER)
            ) return;
            this.name = name;
            this.value = shouldUseInt ? Integer.parseInt(value.toString()) : value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAsInt() {
            if (!shouldUseInt)
                return 0;
            return (int) value;
        }

        public String getAsText() {
            if (shouldUseInt)
                return getAsInt().toString();
            return (String) value;
        }
    }
}
