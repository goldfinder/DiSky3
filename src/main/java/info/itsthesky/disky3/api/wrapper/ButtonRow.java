package info.itsthesky.disky3.api.wrapper;

import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ButtonRow {

    private List<Button> buttons;

    public ButtonRow(final List<Button> buttons) {
        this.buttons = buttons;
    }

    public void clearButtons() { buttons = new ArrayList<>(); }

    public ButtonRow() {
        this.buttons = new ArrayList<>();
    }

    public void addButton(Button builder) {
        this.buttons.add(builder);
    }

    public List<Button> getButtons() {
        return buttons;
    }

    @Override
    public String toString() {
        return "ButtonRow{" +
                "buttons=" + buttons +
                '}';
    }
}