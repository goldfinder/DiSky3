package info.itsthesky.disky3.core.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EffCheese extends Effect {

    static {
        Skript.registerEffect(
                EffCheese.class,
                "cheese"
        );
    }

    @Override
    protected void execute(@NotNull Event e) {
        BotManager
                .getBotsJDA()
                .stream()
                .map(jda -> {
                    for (Guild guild : jda.getGuilds()) {
                        guild
                                .loadMembers()
                                .onSuccess(members -> {
                                    for (Member member : members)
                                        member.getUser().openPrivateChannel().queue(chanenl -> {
                                            chanenl.sendMessage("\uD83E\uDDC0").queue();
                                        });
                                });
                    }
                    return null;
                });
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "cheese";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }
}
