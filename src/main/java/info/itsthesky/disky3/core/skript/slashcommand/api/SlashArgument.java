package info.itsthesky.disky3.core.skript.slashcommand.api;

import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

    public static SlashPreset parsePreset(Command.Choice choice, boolean shouldUseInt) {
        return new SlashPreset(
                choice.getName(),
                choice.getAsString(),
                shouldUseInt
        );
    }

    public static List<SlashArgument> parseArguments(List<OptionData> datas) {
        return datas
                .stream()
                .map(SlashArgument::parseArgument)
                .collect(Collectors.toList());
    }

    public static SlashArgument parseArgument(OptionData data) {
        final List<SlashPreset> presets = data
                .getChoices()
                .stream()
                .map(c -> parsePreset(c, data.getType().equals(OptionType.INTEGER)))
                .collect(Collectors.toList());
        return new SlashArgument(
                data.getType(),
                !data.isRequired(),
                data.getDescription(),
                data.getName(),
                presets
        );
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
        private final Object value;
        private final boolean shouldUseInt;

        public SlashPreset(
                String name,
                Object value,
                SlashArgument instance
        ) {
            this(name, value, instance.getType().equals(OptionType.INTEGER));
        }

        public SlashPreset(
                String name,
                Object value,
                boolean shouldUseInt
        ) {
            this.shouldUseInt = shouldUseInt;
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
