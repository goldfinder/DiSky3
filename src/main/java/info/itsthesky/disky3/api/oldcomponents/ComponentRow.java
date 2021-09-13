package info.itsthesky.disky3.api.oldcomponents;

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Component;

import java.util.*;
import java.util.stream.Collectors;

public class ComponentRow {

    private List<Component> components;

    public ComponentRow() {
        this(new Component[0]);
    }

    public ComponentRow(ActionRow original) {
        this(original.getComponents().toArray(new Component[0]));
    }

    public ComponentRow(Component... components) {
        this.components = Arrays.asList(components);
        if (this.components == null || this.components.isEmpty())
            this.components = new ArrayList<>();
    }

    public void addComponents(Component... component) {
        this.components.addAll(Arrays.asList(component));
    }

    public void addComponents(Collection<Component> components) {
        addComponents(components.toArray(new Component[0]));
    }

    public void clear() {
        this.components.clear();
    }

    public List<Component> getComponents() {
        return components == null ? new ArrayList<>() : components;
    }

    public static List<ActionRow> convert(List<ComponentRow> rows) {
        final List<ActionRow> actionRows = new ArrayList<>();
        for (ComponentRow row : rows)
            actionRows.add(ActionRow.of(row.getComponents()));
        return actionRows;
    }

    public static List<ComponentRow> unConvert(List<ActionRow> rows) {
        List<ComponentRow> rows1 = rows.stream().map(ComponentRow::new).collect(Collectors.toList());
        if (rows1 == null)
            rows1 = new ArrayList<>();
        return rows1;
    }

    public static List<ActionRow> convert(ComponentRow[] rows) {
        return convert(Arrays.asList(rows));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("component row with components");

        for (Component component : components)
            builder.append(" ").append(component.getId()).append(",");

        return builder.toString();
    }
}
