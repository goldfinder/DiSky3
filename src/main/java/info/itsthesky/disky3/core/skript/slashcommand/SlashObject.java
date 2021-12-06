package info.itsthesky.disky3.core.skript.slashcommand;

import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.log.ParseLogHandler;
import ch.njol.skript.log.SkriptLogger;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class SlashObject {

    private static final HashMap<String, SlashObject> REGISTERED = new HashMap<>();

    public static HashMap<String, SlashObject> getRegistered() {
        return REGISTERED;
    }
    public static void clear() {
        try {
            final Collection<SlashObject> temps = Arrays.asList(REGISTERED.values().toArray(new SlashObject[0]));
            temps.forEach(obj -> obj.delete(true));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static List<SlashObject> getLoaded() {
        return Arrays.asList(getRegistered().values().toArray(new SlashObject[0]));
    }

    public static List<SlashObject> fromGuild(Guild guild) {
        return REGISTERED
                .values()
                .stream()
                .filter(slash -> !slash.isGlobal())
                .filter(slash -> slash.getGuildID().equalsIgnoreCase(guild.getId()))
                .collect(Collectors.toList());
    }

    public static List<SlashObject> fromBot(Bot bot) {
        return REGISTERED
                .values()
                .stream()
                .filter(SlashObject::isGlobal)
                .filter(slash -> slash.getBotName().equalsIgnoreCase(bot.getName()))
                .collect(Collectors.toList());
    }

    public static SlashObject get(String name) { return REGISTERED.get(name); }
    public static void remove(SlashObject object) {
        REGISTERED.remove(object.getName());
    }
    public static void register(SlashObject object) {
        REGISTERED.put(object.getName(), object);
    }

    private final String name;
    private final List<SlashArgument> options;
    private final String instanceID;
    private final String botName;
    private final List<TriggerItem> code;
    private final @Nullable String guildID;

    public SlashObject(String name, List<SlashArgument> options, String instanceID, String botName, List<TriggerItem> code, @Nullable String guildID) {
        this.name = name;
        this.options = options;
        this.instanceID = instanceID;
        this.botName = botName;
        this.code = code;
        this.guildID = guildID;
    }

    public SlashObject(String name, List<SlashArgument> options, String instanceID, String botName, List<TriggerItem> code) {
        this(name, options, instanceID, botName, code, null);
    }

    public boolean isGlobal() {
        return guildID == null;
    }

    public String getName() {
        return name;
    }

    public List<SlashArgument> getOptions() {
        return options;
    }

    public String getInstanceID() {
        return instanceID;
    }

    public List<TriggerItem> getCode() {
        return code;
    }

    public String getBotName() {
        return botName;
    }

    public @Nullable String getGuildID() {
        return guildID;
    }

    public void delete() {
        delete(false);
    }

    public void delete(boolean force) {
        final Bot bot = BotManager.searchFromName(getBotName());
        if (isGlobal()) {
            if (force) {
                bot.getCore().deleteCommandById(getInstanceID()).complete();
            } else {
                bot.getCore().deleteCommandById(getInstanceID()).queue();
            }
        } else {
            final Guild guild = bot.getCore().getGuildById(getGuildID());
            if (force) {
                guild.deleteCommandById(getInstanceID()).complete();
            } else {
                guild.deleteCommandById(getInstanceID()).queue();
            }
        }
        remove(this);
    }

    public boolean execute(SlashEvent event) {
        ParseLogHandler log = SkriptLogger.startParseLogHandler();

        try {

            // again, bukkit apis are mostly sync so run it on the main thread
            Utils.sync(() -> {
                if (code.isEmpty())
                    return;
                TriggerItem.walk(code.get(0), event);
            });

        } finally {

            log.stop();

        }

        return true;
    }
}
