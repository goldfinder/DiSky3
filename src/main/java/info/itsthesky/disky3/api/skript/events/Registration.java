package info.itsthesky.disky3.api.skript.events;

import ch.njol.skript.lang.SkriptEventInfo;
import ch.njol.util.StringUtils;

/**
 * Created by Blitz on 7/22/2017.
 */
// Vixio.registerCondition(cls, patterns).setName().setDesc();
public class Registration {

    private String name;
    private String[] desc;
    private String example;
    private String splitExample;
    private Class<?> clazz;
    private String[] syntaxes;
    private String userFacing;
    private Class event;
    private SkriptEventInfo<?> holder;

    public Registration(Class<?> cls, SkriptEventInfo<?> holder, String... syntaxes) {
        clazz = cls;
        this.syntaxes = syntaxes;
        this.holder = holder;
    }

    public Registration apply() {
        if (holder == null)
            return this;
        if (desc != null)
            holder.description(desc);
        if (example != null)
            holder.examples(example);
        return this;
    }

    public Registration(String... syntaxes) {
        this.syntaxes = syntaxes;
    }

    public Registration setExample(String s) {
        String example = s
                .replaceAll("\t", "\\\\t")
                .replaceAll("\"", "\\\\\"");
        this.example = example;
        return apply();
    }

    public String getName() {
        return this.name;
    }

    public Registration setName(String s) {
        this.name = s;
        return apply();
    }

    public String[] getDesc() {
        return this.desc;
    }

    public Registration setDesc(String... s) {
        this.desc = s;
        return apply();
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String[] getSyntaxes() {
        return this.syntaxes;
    }

    public String getSyntax() {
        return this.syntaxes[0];
    }

    public String getExample() {
        return this.example;
    }

    public Registration setExample(String... s) {
        String example = StringUtils.join(s, ",")
                .replaceAll("\t", "\\\\t")
                .replaceAll("\"", "\\\"");
        splitExample = StringUtils.join(s, "\n");
        return setExample(example);
    }

    public String getUserFacing() {
        return this.userFacing;
    }

    public Registration setUserFacing(String s) {
        this.userFacing = s;
        return apply();
    }

    public Registration setUserFacing(String... patterns) {
        this.syntaxes = patterns;
        return apply();
    }

    public Registration setEvent(Class clazz) {
        this.event = clazz;
        return apply();
    }

    public Class getEvent() {
        return event;
    }

    public String getSplitExample() {
        if (splitExample == null) {
            return this.example.replaceAll("\\\\\"", "\"");
        }
        return splitExample.replaceAll("\\\\\"", "\"");
    }
}