package info.itsthesky.disky3.core.skript.slashcommand;

import ch.njol.util.NonNullPair;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlashArgument {

    private final OptionType type;
    private final boolean optional;
    private final String desc;
    private final String name;

    public SlashArgument(
            OptionType type,
            boolean optional,
            String desc
    ) {
        this(type, optional, desc, type.name().toLowerCase(Locale.ROOT).replaceAll("_", " "));
    }

    public SlashArgument(
            OptionType type,
            boolean optional,
            String desc,
            String name
    ) {
        this.optional = optional;
        this.type = type;
        this.desc = desc;
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public OptionType getType() {
        return type;
    }

    public boolean isOptional() {
        return optional;
    }

    public static NonNullPair<String, String> parseArgumentValues(String input) {

        final Pattern pattern = Pattern.compile("\"(.+)\",( )?\"(.+)\"");
        final Matcher matcher = pattern.matcher(input);
        if (!matcher.matches())
            return null;

        final String name = matcher.group(1);
        final String desc = matcher.group(3);

        if (name == null || desc == null)
            return null;

        return new NonNullPair<>(name, desc);
    }
}
