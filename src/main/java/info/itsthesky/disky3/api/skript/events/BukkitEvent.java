package info.itsthesky.disky3.api.skript.events;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Made by Blitz, minor edit by Sky for DiSky
 */
public class BukkitEvent extends org.bukkit.event.Event {

    private static final HandlerList handlerList = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}