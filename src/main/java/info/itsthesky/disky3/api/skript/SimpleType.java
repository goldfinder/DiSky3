package info.itsthesky.disky3.api.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class SimpleType<T> extends ClassInfo<T> implements Changer<T> {

    private final String variableName;
    private final String name;
    private final String pattern;
    private final Class<T> clz;

    public SimpleType(Class<T> clz, String name) {
        this(clz, name, name, ".+");
    }

    public SimpleType(Class<T> clz, String name, String pattern) {
        this(clz, name, pattern, ".+");
    }

    public SimpleType(Class<T> clz, String name, String pattern, String variableName) {
        super(clz, name.toLowerCase().replaceAll("\\s+", ""));
        this.clz = clz;
        this.name = name;
        this.pattern = pattern;
        this.variableName = variableName;
        register();
    }

    public abstract String toString(T arg0, int arg1);

    public String toVariableNameString(T arg0) {
        return toString(arg0, 0);
    }

    public T parse(String arg0, ParseContext arg1) {
        return null;
    }

    public boolean canParse(ParseContext pc) {
        return true;
    }

    @Override
    public Class<?> @NotNull [] acceptChange(@NotNull ChangeMode mode) {
        return null;
    }

    @Override
    public void change(T @NotNull [] source, Object @NotNull [] set, @NotNull ChangeMode mode) {

    }

    private void register() {
        try {
            Classes.registerClass(user(pattern)
                    .defaultExpression(new EventValueExpression<>(clz))
                    .parser(new Parser<T>() {
                        @Override
                        public @NotNull String getVariableNamePattern() {
                            return variableName;
                        }

                        @Override
                        public boolean canParse(@NotNull ParseContext context) {
                            return SimpleType.this.canParse(context);
                        }

                        @Override
                        @Nullable
                        public T parse(@NotNull String arg0, @NotNull ParseContext arg1) {
                            return SimpleType.this.parse(arg0, arg1);
                        }

                        @Override
                        public @NotNull String toString(@NotNull T arg0, int arg1) {
                            return SimpleType.this.toString(arg0, arg1);
                        }

                        @Override
                        public @NotNull String toVariableNameString(@NotNull T arg0) {
                            return SimpleType.this.toVariableNameString(arg0);
                        }
                    }));
        } catch (Throwable e) {
            Skript.warning("Couldn't register the type '" + name + "'. Due to: " + (e.getMessage() != null && !e.getMessage().isEmpty() ? e.getMessage() : "unknown"));
        }

    }

}