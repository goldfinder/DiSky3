package info.itsthesky.disky3.core.events.category;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.skript.events.specific.LogEvent;
import info.itsthesky.disky3.api.skript.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdatePositionEvent;
import org.jetbrains.annotations.NotNull;

public class CategoryPosition extends DiSkyEvent<ChannelUpdatePositionEvent> {

    @Override
    public boolean check(ChannelUpdatePositionEvent event) {
        return event.isFromType(ChannelType.CATEGORY);
    }

    static {
        DiSkyEvent.register("Category Position", CategoryPosition.class, EvtCategoryName.class,
                "category position (change|update)")
                .setName("Category Name");

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

    public static class EvtCategoryName extends SimpleDiSkyEvent<ChannelUpdatePositionEvent> implements LogEvent {
        public EvtCategoryName(CategoryPosition event) { }

        @Override
        public User getActionAuthor() {
            return ((Category) getJDAEvent().getChannel()).getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}