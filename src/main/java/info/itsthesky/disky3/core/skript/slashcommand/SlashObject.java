package info.itsthesky.disky3.core.skript.slashcommand;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.log.ParseLogHandler;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.util.Utils;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.Guild;

import java.io.File;
import java.util.List;

public class SlashObject {

    private final String name;
    private final Expression<String> description;
    private final List<String> aliases;
    private final List<String> bots;
    private final List<String> guilds;
    private final List<String> allowedRoles;
    private final List<String> disallowedRoles;

    private final Trigger trigger;

    private final List<SlashArgument> arguments;

    public SlashObject(File script, String name, List<SlashArgument> arguments,
                       List<String> aliases, Expression<String> description, List<String> bots, List<TriggerItem> items,
                       List<String> guilds,
                       List<String> allowedRoles, List<String> disallowedRoles
                       ) {
        this.name = name;
        if (aliases != null) {
            aliases.removeIf(alias -> alias.equalsIgnoreCase(name));
        }
        this.aliases = aliases;
        this.guilds = guilds;
        this.allowedRoles = allowedRoles;
        this.disallowedRoles = disallowedRoles;
        this.description = description;
        this.bots = bots;
        this.arguments = arguments;
        trigger = new Trigger(script, "discord command " + name, new SimpleEvent(), items);

    }

    public boolean execute(SlashEvent event) {
        ParseLogHandler log = SkriptLogger.startParseLogHandler();

        try {

            if (!bots.isEmpty()) {
                try {
                    if (bots != null && !bots.contains(BotManager.searchFromJDA(event.getBot()).getName())) {
                        return false;
                    }
                } catch (NullPointerException ignored) { }
            }

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

    public List<String> getGuilds() {
        return guilds;
    }

    public Expression<String> getDescription() {
        return description;
    }

    public List<String> getBots() {
        return bots;
    }

    public List<String> getAllowedRoles() {
        return allowedRoles;
    }

    public List<String> getDisallowedRoles() {
        return disallowedRoles;
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