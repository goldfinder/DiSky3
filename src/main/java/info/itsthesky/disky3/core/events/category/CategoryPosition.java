package info.itsthesky.disky3.core.events.category;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdatePositionEvent;

public class CategoryPosition extends DiSkyEvent<CategoryUpdatePositionEvent> {

    static {
        DiSkyEvent.register("Category Position", CategoryPosition.class, EvtCategoryPosition.class,
                "category position (change|update)")
                .setName("Category Position");


       EventValues.registerEventValue(EvtCategoryPosition.class, Category.class, new Getter<Category, EvtCategoryPosition>() {
            @Override
            public Category get(EvtCategoryPosition event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

       EventValues.registerEventValue(EvtCategoryPosition.class, Guild.class, new Getter<Guild, EvtCategoryPosition>() {
            @Override
            public Guild get(EvtCategoryPosition event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtCategoryPosition.class, Category.class, new Getter<Category, EvtCategoryPosition>() {
            @Override
            public Category get(EvtCategoryPosition event) {
                return event.getJDAEvent().getCategory();
            }
        }, 0);

       EventValues.registerEventValue(EvtCategoryPosition.class, Bot.class, new Getter<Bot, EvtCategoryPosition>() {
            @Override
            public Bot get(EvtCategoryPosition event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtCategoryPosition extends SimpleDiSkyEvent<CategoryUpdatePositionEvent> implements LogEvent {
        public EvtCategoryPosition(CategoryPosition event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}