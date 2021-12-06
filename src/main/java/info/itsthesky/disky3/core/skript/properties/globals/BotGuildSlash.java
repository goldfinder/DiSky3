package info.itsthesky.disky3.core.skript.properties.globals;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.core.skript.slashcommand.SlashObject;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("All Slash Commands")
@Description({"Get every loaded slash commands from three specific side:",
        "- Bot, registered globally and will only return bot's slash command",
        "- Guild, loaded per guild and will return slash command of the specified guild",
        "- All, every loaded commands from both bots & guilds",
        "These are only wrapper to cache Discord's slash command, and therefore only offer a view-way of these entities!",
        "(= you cannot change name, description, etc... but can get them only)"})
@Examples({"all loaded slash commands",
        "slash cmds from event-guild",
        "slash commands of bot \"name\""})
public class BotGuildSlash extends SimpleExpression<SlashObject> {

    static {
        Skript.registerExpression(
                BotGuildSlash.class,
                SlashObject.class,
                ExpressionType.COMBINED,
                "[all] [loaded] slash [(cmd|command)[s]] (of|from|in) %bot/guild%",
                "[all] [loaded] slash [(cmd|command)[s]] loaded [on discord]"
        );
    }

    private Expression<Object> exprEntity;
    private boolean global;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprEntity = (Expression<Object>) exprs[0];
        global = matchedPattern == 1;
        return true;
    }

    @Override
    protected SlashObject @NotNull [] get(@NotNull Event e) {
        final Object entity = Utils.verifyVar(e, exprEntity);
        if (!global && entity == null)
            return new SlashObject[0];
        return (global ? SlashObject.getLoaded() : (
                entity instanceof Guild ?
                        SlashObject.fromGuild((Guild) entity)
                        : SlashObject.fromBot((Bot) entity))
        ).toArray(new SlashObject[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends SlashObject> getReturnType() {
        return SlashObject.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return global ? "all loaded slash commands" : "all slash commands from " + exprEntity.toString(e, debug);
    }

}
