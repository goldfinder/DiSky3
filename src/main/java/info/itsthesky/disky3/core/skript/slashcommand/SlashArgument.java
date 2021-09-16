package info.itsthesky.disky3.core.skript.slashcommand;

import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Locale;

public class SlashArgument {

    private final OptionType type;
    private final boolean optional;
    private final String desc;

    public SlashArgument(
            OptionType type,
            boolean optional,
            String desc
    ) {
        this.optional = optional;
        this.type = type;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return getType().name().replace("_", " ").toLowerCase(Locale.ROOT);
    }

    public OptionType getType() {
        return type;
    }

    public boolean isOptional() {
        return optional;
    }
}
