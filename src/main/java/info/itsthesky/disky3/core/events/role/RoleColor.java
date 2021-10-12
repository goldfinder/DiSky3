package info.itsthesky.disky3.core.events.role;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class RoleColor extends DiSkyEvent<RoleUpdateColorEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", RoleColor.class, EvtRoleColor.class,
                "[discord] role color (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtRoleColor.class, Color.class, new Getter<Color, EvtRoleColor>() {
            @Override
            public Color get(@NotNull EvtRoleColor event) {
                return event.getJDAEvent().getNewColor();
            }
        }, 1);

        EventValues.registerEventValue(EvtRoleColor.class, Color.class, new Getter<Color, EvtRoleColor>() {
            @Override
            public Color get(@NotNull EvtRoleColor event) {
                return event.getJDAEvent().getOldColor();
            }
        }, -1);

        EventValues.registerEventValue(EvtRoleColor.class, Color.class, new Getter<Color, EvtRoleColor>() {
            @Override
            public Color get(@NotNull EvtRoleColor event) {
                return event.getJDAEvent().getOldColor();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleColor.class, Guild.class, new Getter<Guild, EvtRoleColor>() {
            @Override
            public Guild get(@NotNull EvtRoleColor event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleColor.class, Role.class, new Getter<Role, EvtRoleColor>() {
            @Override
            public Role get(@NotNull EvtRoleColor event) {
                return event.getJDAEvent().getRole();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleColor.class, Bot.class, new Getter<Bot, EvtRoleColor>() {
            @Override
            public Bot get(@NotNull EvtRoleColor event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtRoleColor extends SimpleDiSkyEvent<RoleUpdateColorEvent> implements LogEvent {
        public EvtRoleColor(RoleColor event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}