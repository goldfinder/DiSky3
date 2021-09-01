package info.itsthesky.disky3.core.events.category;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdateNameEvent;

public class CategoryName extends DiSkyEvent<CategoryUpdateNameEvent> {

    static {
        DiSkyEvent.register("Category Name", CategoryName.class, EvtCategoryName.class,
                "category name (change|update)")
                .setName("Category Name");

       EventValues.registerEventValue(EvtCategoryName.class, Category.class, new Getter<Category, EvtCategoryName>() {
            @Override
            public Category get(EvtCategoryName event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

       EventValues.registerEventValue(EvtCategoryName.class, Guild.class, new Getter<Guild, EvtCategoryName>() {
            @Override
            public Guild get(EvtCategoryName event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtCategoryName.class, Category.class, new Getter<Category, EvtCategoryName>() {
            @Override
            public Category get(EvtCategoryName event) {
                return event.getJDAEvent().getCategory();
            }
        }, 0);

       EventValues.registerEventValue(EvtCategoryName.class, Bot.class, new Getter<Bot, EvtCategoryName>() {
            @Override
            public Bot get(EvtCategoryName event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtCategoryName extends SimpleDiSkyEvent<CategoryUpdateNameEvent> implements LogEvent {
        public EvtCategoryName(CategoryName event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}