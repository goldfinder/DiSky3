package info.itsthesky.disky3.core.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.section.RestExceptionSection;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

@Name("Purge Amount of Message")
@Description("Grab all X latest message of a text channel and purge (= delete) them. " +
        "\nIf the force is specified, then it will purge only the right messages:" +
        "\nFor example, grabbing 50 messages where 30 are older than 2 weeks, using the force pattern DiSky will delete the 20 other without throwing an exception.")
@Examples("purge last 50 messages from event-channel")
public class EffPurgeMessages extends RestExceptionSection<Void> {

    public static final HashMap<String, Boolean> PURGED_MESSAGES = new HashMap<>();

    static {
        register(EffPurgeMessages.class,
                "purge [all] last[est] %number% messages (from|in|of) [the] [channel] %channel/textchannel%",
                "force purge [all] last[est] %number% messages (from|in|of) [the] [channel] %channel/textchannel%");
    }

    private Expression<Number> exprAmount;
    private Expression<Object> exprEntity;
    private boolean force;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprAmount = (Expression<Number>) exprs[0];
        exprEntity = (Expression<Object>) exprs[1];
        force = matchedPattern == 1;
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "purge last " + exprAmount.toString(e, debug) + " messages from channel " + exprEntity.toString(e, debug);
    }

    @Override
    public RestAction<Void> runRestAction(Event e) {
        Object entity = exprEntity.getSingle(e);
        Number amount = exprAmount.getSingle(e);
        if (entity == null || amount == null) return null;

        GuildMessageChannel channel;
        try {
            channel = (GuildMessageChannel) entity;
        } catch (Exception ex) { return null; }

        if (channel == null) return null;
        if (Utils.round(amount.doubleValue()) < 0 && Utils.round(amount.doubleValue()) > 100) {
            DiSky.getInstance().getLogger()
                    .warning("DiSky can't purge more than 100 messages, and you're trying to purge "+Utils.round(amount.doubleValue())+"! Set to 100.");
            amount = 100;
        }
        int finalValue = Utils.round(amount.doubleValue());
        List<Message> messages = channel.getHistory().retrievePast(finalValue).complete();
        if (force) {
            OffsetDateTime twoWeeksAgo = OffsetDateTime.now().minus(2, ChronoUnit.WEEKS);
            messages.removeIf(m -> m.getTimeCreated().isBefore(twoWeeksAgo));
        }
        return channel.deleteMessages(messages);
    }
}