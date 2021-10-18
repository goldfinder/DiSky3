package info.itsthesky.disky3.core.events.category;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNameEvent;
import org.jetbrains.annotations.NotNull;

public class CategoryName extends DiSkyEvent<ChannelUpdateNameEvent> {

    @Override
    public boolean check(ChannelUpdateNameEvent event) {
        return event.isFromType(ChannelType.CATEGORY);
    }

    static {
        DiSkyEvent.register("Guild Category Name", CategoryName.class, EvtCategoryName.class,
                "[discord] [guild] category name (change|update)")
                .setName("Guild Category Name")
                .setDesc("Fired when the name of a category changes.")
                .setExample("on guild category name change:");

       EventValues.registerEventValue(EvtCategoryName.class, Category.class, new Getter<Category, EvtCategoryName>() {
            @Override
            public Category get(@NotNull EvtCategoryName event) {
                return (Category) event.getJDAEvent().getEntity();
            }
        }, 0);

       EventValues.registerEventValue(EvtCategoryName.class, Guild.class, new Getter<Guild, EvtCategoryName>() {
            @Override
            public Guild get(@NotNull EvtCategoryName event) {
                return ((Category) event.getJDAEvent().getChannel()).getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtCategoryName.class, Category.class, new Getter<Category, EvtCategoryName>() {
            @Override
            public Category get(@NotNull EvtCategoryName event) {
                return (Category) event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtCategoryName.class, Bot.class, new Getter<Bot, EvtCategoryName>() {
            @Override
            public Bot get(@NotNull EvtCategoryName event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtCategoryName extends SimpleDiSkyEvent<ChannelUpdateNameEvent> implements LogEvent {
        public EvtCategoryName(CategoryName event) { }

        @Override
        public User getActionAuthor() {
            return ((Category) getJDAEvent().getChannel()).getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}