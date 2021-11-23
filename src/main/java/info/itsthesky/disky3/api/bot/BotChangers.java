package info.itsthesky.disky3.api.bot;

import ch.njol.skript.classes.Converter;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

public class BotChangers {

    public static final HashMap<Class<?>, BotConverter<?>> CONVERTERS = new HashMap<>();

    private static <T> void register(BotConverter<T> converter, Class<T> clazz) {
        CONVERTERS.put(clazz, converter);
    }

    @SuppressWarnings("unchecked")
    public static <T> T convert(T entity, Bot bot) {
        final BotConverter<T> converter = (BotConverter<T>) CONVERTERS.getOrDefault(entity.getClass(), null);
        if (converter == null)
            return null;
        return converter.convertSafe(entity, bot);
    }

    private static <T> void register(Class<T> clazz) {
        if (!clazz.isAssignableFrom(ISnowflake.class))
            throw new UnsupportedOperationException();
        register(new BotConverter<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public T convert(T entity, Bot bot) throws Exception {
                final String methodName = "get" + entity.getClass().getSimpleName().replaceAll("Impl", "") + "ById";
                System.out.println(methodName);
                final Method method = bot.getCore().getClass().getDeclaredMethod(methodName, String.class);
                method.setAccessible(true); // Should not be needed but nvm
                return (T) method.invoke(bot, ((ISnowflake) entity).getId());
            }
        }, clazz);
    }

    private static void register(Class<?>... classes) {
        Arrays.stream(classes).filter(c -> c.isAssignableFrom(ISnowflake.class)).forEach(BotChangers::register);
    }

    static {
        register(Guild.class, Member.class, User.class,
                GuildChannel.class, TextChannel.class, VoiceChannel.class, StageChannel.class, NewsChannel.class,
                Role.class, UpdatingMessage.class);
    }

    public static abstract class BotConverter<E> {

        public abstract E convert(E entity, Bot bot) throws Exception;

        public E convertSafe(E entity, Bot bot){
            try {
                return convert(entity, bot);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        };

    }
}
