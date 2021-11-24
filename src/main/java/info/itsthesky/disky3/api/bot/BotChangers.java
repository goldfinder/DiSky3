package info.itsthesky.disky3.api.bot;

import ch.njol.skript.classes.Converter;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.internal.JDAImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class BotChangers {

    public static final HashMap<Class<?>, BiFunction<?, Bot, ?>> CONVERTERS = new HashMap<>();

    private static <T> void register(BiFunction<T, Bot, T> converter, Class<T> clazz) {
        CONVERTERS.put(clazz, converter);
    }

    @SuppressWarnings("unchecked")
    public static <T> T convert(T entity, Bot bot) {
        final BiFunction<T, Bot, T> converter = (BiFunction<T, Bot, T>) CONVERTERS.getOrDefault(entity.getClass().getInterfaces()[0], null);
        if (converter == null)
            return null;
        return converter.apply(entity, bot);
    }

    private static <T> void register(Class<T> clazz, BiFunction<T, Bot, T> converter) {
        CONVERTERS.put(clazz, converter);
    }

    public static void init() {
        register(Guild.class, (entity, bot) -> bot.getCore().getGuildById(entity.getId()));
        register(User.class, (entity, bot) -> bot.getCore().getUserById(entity.getId()));
        register(Member.class, (entity, bot) -> bot.getCore().getGuildById(entity.getGuild().getId()).getMemberById(entity.getId()));

        register(GuildChannel.class, (entity, bot) -> bot.getCore().getGuildChannelById(entity.getId()));
        register(TextChannel.class, (entity, bot) -> bot.getCore().getTextChannelById(entity.getId()));
        register(NewsChannel.class, (entity, bot) -> bot.getCore().getNewsChannelById(entity.getId()));
        register(StageChannel.class, (entity, bot) -> bot.getCore().getStageChannelById(entity.getId()));
        register(ThreadChannel.class, (entity, bot) -> bot.getCore().getThreadChannelById(entity.getId()));
        register(VoiceChannel.class, (entity, bot) -> bot.getCore().getVoiceChannelById(entity.getId()));

        register(UpdatingMessage.class, (entity, bot) ->
                UpdatingMessage.from(bot
                        .getCore()
                        .getGuildById(entity.getMessage().getGuild().getId())
                        .getTextChannelById(entity.getMessage().getTextChannel().getId())
                        .getHistory()
                        .getMessageById(entity.getId()))
        );
    }
}
