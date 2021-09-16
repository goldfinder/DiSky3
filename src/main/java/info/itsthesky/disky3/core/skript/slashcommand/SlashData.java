package info.itsthesky.disky3.core.skript.slashcommand;

public class SlashData {
    private final String name;
    private final SlashObject command;

    public SlashData(String name, SlashObject command) {
        this.name = name;
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public SlashObject getCommand() {
        return command;
    }
}
