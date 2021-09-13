package info.itsthesky.disky3.core.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.emojis.Emote;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ExprEmoji extends SimpleExpression<Emote> {
    static {
        Skript.registerExpression(ExprEmoji.class, Emote.class, ExpressionType.SIMPLE,
                "(emoji|emote|reaction)[s] %strings% [(from|in) %-guild%]");
    }

    private Expression<String> name;
    private Expression<Guild> guild;

    @Override
    protected Emote @NotNull [] get(@NotNull Event e) {
        String[] emotes = name.getAll(e);
        Guild guild = this.guild == null ? null : this.guild.getSingle(e);
        if (emotes.length == 0) return new Emote[0];
        return convert(guild, emotes).toArray(new Emote[0]);
    }

    public static List<Emote> convert(@Nullable Guild guild, String... emotes) {
        List<Emote> emojis = new ArrayList<>();
        List<Emote> finalEmotes = new ArrayList<>();
        for (String input : emotes) {
            emojis.add(Utils.unicodeFrom(input, guild));
        }
        for (Emote emote1 : emojis) {
            if (!emote1.getAsMention().contains(":")) {
                finalEmotes.add(emote1);
                continue;
            }
            if (emote1.getAsMention().startsWith("<:") && emote1.getAsMention().endsWith(">"))
                finalEmotes.add(emote1);
        }
        return finalEmotes;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Emote> getReturnType() {
        return Emote.class;
    }

    @Override
    public @NotNull String toString(Event e, boolean debug) {
        return "emoji named " + name.toString(e, debug) + (guild == null ? "" : " from " + guild.toString(e, debug));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        name = (Expression<String>) exprs[0];
        guild = (Expression<Guild>) exprs[1];
        return true;
    }
}