package info.itsthesky.disky3.core.skript.slashcommand.api;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ItsTheSky
 */
@Deprecated
public class PatternGroup {

    private final static String TEXT_ENTRY = "[\\w |]+";
    private final StringBuilder pattern;
    private final HashMap<String, Integer> indexes;
    private String applyString = null;
    private int textGroupIndex = 3;

    public PatternGroup() {
        this(new StringBuilder(), new HashMap<>());
    }

    private PatternGroup(
            StringBuilder pattern,
            HashMap<String, Integer> indexes
    ) {
        this.indexes = indexes;
        this.pattern = pattern;
    }

    public PatternGroup addEntry(String entryName, boolean optional) {
        if (isWrong(entryName))
            throw new IllegalArgumentException("Wrong regex format.");
        if (indexes.containsKey(entryName))
            throw new IllegalArgumentException("The entry named " + entryName + " is already present in this PatternGroup.");

        indexes.put(entryName, textGroupIndex);
        if (!pattern.toString().isEmpty()) {
            if (optional) {
                pattern.append("(,)?( )?(").append(entryName).append("( )?=( )?\"("+TEXT_ENTRY+")\")?");
                textGroupIndex += 6;
            } else {
                pattern.append("(,)?( )?").append(entryName).append("( )?=( )?\"("+TEXT_ENTRY+")\"");
                textGroupIndex += 5;
            }
        } else {
            if (optional) {
                pattern.append("(").append(entryName).append("( )?=( )?\"("+TEXT_ENTRY+")\")?");
                textGroupIndex += 4;
            } else {
                pattern.append(entryName).append("( )?=( )?\"("+TEXT_ENTRY+")\"");
                textGroupIndex += 3;
            }
        }
        return this;
    }

    public PatternGroup copy() {
        return new PatternGroup(this.pattern, this.indexes);
    }

    public boolean apply(String input) {
        if (isWrong(input))
            throw new IllegalArgumentException("Wrong regex format.");
        if (!Pattern.compile(pattern.toString()).matcher(input).matches())
            return false;
        this.applyString = input;
        return true;
    }

    public boolean isApplied() {
        return applyString != null;
    }

    public String getEntry(String entryName) {
        if (!isApplied())
            throw new IllegalStateException("This PatternGroup haven't got any apply string.");
        if (!indexes.containsKey(entryName))
            throw new IllegalArgumentException("Unknown entry name " + entryName);

        Matcher matcher = Pattern.compile(pattern.toString()).matcher(applyString);
        return matcher.group(indexes.get(entryName) - 1);
    }

    public Pattern getPattern() {
        return Pattern.compile(pattern.toString());
    }

    private static boolean isWrong(String input) {
        try {
            Pattern.compile(input);
            return false;
        } catch (Exception ex) {
            return true;
        }
    }
}
