package info.itsthesky.disky3.core.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.emojis.Emote;
import info.itsthesky.disky3.api.emojis.updated.Emojis;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    public List<Emote> convert(@Nullable Guild guild, String... emotes) {
        List<Emote> emojis = new ArrayList<>();
        for (String input : emotes) {

            Emote emote;
            try {
                emote = new Emote(input.toLowerCase(Locale.ROOT), Emojis.ofShortcode(input.toLowerCase(Locale.ROOT)).unicode());
            } catch (NullPointerException ex) {
                final boolean useID = input.matches("[^0-9]");
                if (guild == null) {

                    emote = BotManager
                            .getBotsJDA()
                            .stream()
                            .map(JDA::getGuilds)
                            .map(guilds -> {
                                for (Guild guild1 : guilds) {
                                    return Emote.fromJDA(guild1
                                            .getEmotes()
                                            .stream()
                                            .filter(e -> useID ? e.getId().equalsIgnoreCase(input) : e.getName().equalsIgnoreCase(input))
                                            .findAny()
                                            .orElse(null));
                                }
                                return null;
                            }).findAny().orElse(null);

                } else {

                    emote = Emote.fromJDA(guild
                            .getEmotes()
                            .stream()
                            .filter(e -> useID ? e.getId().equalsIgnoreCase(input) : e.getName().equalsIgnoreCase(input))
                            .findAny()
                            .orElse(null));

                }
            }

            if (emote == null)
            {
                Skript.warning("Cannot found the emote named " + input);
                continue;
            }
            emojis.add(emote);
        }
        return emojis;
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
        if (guild == null)
            guild = Utils.defaultToEventValue(guild, Guild.class);
        new NodeInformation();
        return true;
    }
}