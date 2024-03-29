package info.itsthesky.disky3.core.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.section.EffectSection;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Bot Builder")
@Description("This scope allow you to create a new bot with advanced options, such as intents and flags.")
@Since("3.0")
public class ScopeBotBuilder extends EffectSection {

    public static JDABuilder lastBuilder;

    static {
        Skript.registerCondition(ScopeBotBuilder.class, "make [new] [discord] bot");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (checkIfCondition()) return false;
        if (!hasSection()) return false;

        loadSection(true);

        return true;
    }

    @Override
    protected void execute(Event e) {
        lastBuilder = JDABuilder.createDefault("");
        runSection(e);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "make new discord bot";
    }

}
