package info.itsthesky.disky3.core.skript.properties.globals;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.PropertyCondition;
import info.itsthesky.disky3.api.skript.properties.base.EasyPropertyCondition;
import net.dv8tion.jda.api.entities.GuildThread;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@Name("Member / Bot is in Thread")
@Description({"Check if a specific member or bot is in a guild thread.",
"Useful to avoid exception while using join & leave effects."})
public class BotMemberIsInThread extends EasyPropertyCondition<Object> {

    static {
        register(
                BotMemberIsInThread.class,
                PropertyCondition.PropertyType.BE,
                "in [the] thread %thread%",
                "member/bot"
        );
    }

    private Expression<GuildThread> exprThread;

    @Override
    public boolean check(Event e, Object entity) {
        final String id = entity instanceof Member ? ((Member) entity).getId() : ((Bot) entity).getId();
        final GuildThread thread = exprThread.getSingle(e);
        if (id == null || thread == null)
            return false;
        final boolean contains = thread
                .getMembers()
                .stream()
                .filter(member -> member.getId().equalsIgnoreCase(id))
                .findAny()
                .orElse(null) != null;
        return isNegated() != contains;
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        exprThread = (Expression<GuildThread>) exprs[0];
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }
}
