package info.itsthesky.disky3.core.skript.slashcommand;

import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.log.ParseLogHandler;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.util.Utils;
import info.itsthesky.disky3.api.bot.BotManager;

import java.io.File;
import java.util.List;

public class SlashObject {

    private final String name;
    private final List<String> aliases;
    private final String description;
    private final List<String> bots;

    private final Trigger trigger;

    private final List<SlashArgument> arguments;

    public SlashObject(File script, String name, List<SlashArgument> arguments,
                       List<String> aliases, String description, List<String> bots, List<TriggerItem> items
                       ) {
        this.name = name;
        if (aliases != null) {
            aliases.removeIf(alias -> alias.equalsIgnoreCase(name));
        }
        this.aliases = aliases;
        this.description = Utils.replaceEnglishChatStyles(description);
        this.bots = bots;
        this.arguments = arguments;
        trigger = new Trigger(script, "discord command " + name, new SimpleEvent(), items);

    }

    public boolean execute(SlashEvent event) {
        ParseLogHandler log = SkriptLogger.startParseLogHandler();

        try {

            try {
                if (bots != null && !bots.contains(BotManager.searchFromJDA(event.getBot()).getName())) {
                    return false;
                }
            } catch (NullPointerException ignored) {}

            // again, bukkit apis are mostly sync so run it on the main thread
            info.itsthesky.disky3.api.Utils.sync(() -> trigger.execute(event));

        } finally {
            log.stop();
        }

        return true;
    }

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getBots() {
        return bots;
    }

    public List<SlashArgument> getArguments() {
        return arguments;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public File getScript() {
        return trigger.getScript();
    }

}