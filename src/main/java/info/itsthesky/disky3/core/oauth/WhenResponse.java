package info.itsthesky.disky3.core.oauth;

import bell.oauth.discord.main.Response;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.util.Kleenean;
import com.sun.net.httpserver.HttpExchange;
import info.itsthesky.disky3.api.section.EffectSection;
import info.itsthesky.disky3.api.skript.MultipleEventValue;
import info.itsthesky.disky3.api.skript.events.BukkitEvent;
import info.itsthesky.disky3.api.skript.events.EventValue;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class WhenResponse extends EffectSection {

    static {
        register(
                WhenResponse.class,
                "when [the] [o]auth[2] [system] %oauthmanager% (got|have|receive) [a] response"
        );
    }

    private Expression<OAuthWrapper> exprOAuth;

    @Override
    protected void execute(Event e) {
        final OAuthWrapper wrapper = exprOAuth.getSingle(e);
        if (wrapper == null)
            return;
        OAuthManager.registerWebserver(wrapper, this);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "when the oauth system " + exprOAuth.toString(e, debug) + " receive response";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprOAuth = (Expression<OAuthWrapper>) exprs[0];
        if (checkIfCondition())
            return false;
        loadSection("response receive", true, ResponseEvent.class);
        return true;
    }

    public static class EventGuilds extends MultipleEventValue<Guild, ResponseEvent> {

        static {
            register(
                    EventGuilds.class,
                    Guild.class,
                    "guilds"
            );
        }

        @Override
        protected List<Guild> convert(ResponseEvent event, ValueAge age) {
            return event
                    .getWrapper()
                    .getInstance()
                    .getGuilds()
                    .stream()
                    .map(guild -> event.getWrapper().getBot().getCore().getGuildById(guild.getId()))
                    .collect(Collectors.toList());
        }

        @Override
        protected String getEntityName() {
            return "guilds";
        }

        @Override
        protected Class<ResponseEvent>[] getEventClasses() {
            return new Class[] {ResponseEvent.class};
        }

        @Override
        public @NotNull Class<? extends Guild> getReturnType() {
            return Guild.class;
        }
    }

    public static class ResponseEvent extends BukkitEvent {

        private final HttpExchange exchange;
        private final OAuthWrapper wrapper;
        private final Response response;

        public ResponseEvent(HttpExchange exchange, OAuthWrapper wrapper, Response response) {
            this.exchange = exchange;
            this.wrapper = wrapper;
            this.response = response;
        }

        public Response getResponse() {
            return response;
        }

        public HttpExchange getExchange() {
            return exchange;
        }

        public OAuthWrapper getWrapper() {
            return wrapper;
        }

        static {
            EventValues.registerEventValue(ResponseEvent.class, String.class, new Getter<String, ResponseEvent>() {
                @Override
                public @Nullable String get(ResponseEvent event) {
                    return event.getResponse().name().toLowerCase(Locale.ROOT);
                }
            }, 0);

            EventValues.registerEventValue(ResponseEvent.class, User.class, new Getter<User, ResponseEvent>() {
                @Override
                public @Nullable User get(ResponseEvent event) {
                    return event.getWrapper().getBot().getCore().getUserById(event.getWrapper().getInstance().getUser().getId());
                }
            }, 0);
        }

    }
}
