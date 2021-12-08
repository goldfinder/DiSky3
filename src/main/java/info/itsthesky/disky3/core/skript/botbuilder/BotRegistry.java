package info.itsthesky.disky3.core.skript.botbuilder;

import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SelfRegisteringSkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.log.SkriptLogger;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.BotManager;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BotRegistry extends SelfRegisteringSkriptEvent {

    static {
        Skript.registerEvent("Define Bot", BotRegistry.class, BotParsingEvent.class, "(create|define) [new] bot [(named|with name)] %string%")
                .description("The whole new way to define and manage Bots with DiSky!",
                        "Every main features are unified in one scope and will support sharding too.");
    }

    private BotEntity entity;

    @Override
    public void register(@NotNull Trigger t) { }

    @Override
    public void unregister(@NotNull Trigger t) { }

    @Override
    public void unregisterAll() { }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, SkriptParser.@NotNull ParseResult parseResult) {
        final String name = ((Literal<String>) args[0]).getSingle();

        if (BotManager.isLoaded(name)) {
            Skript.error("The bot named '"+name+"' is already defined (= loaded)!");
            return false;
        }

        final SectionNode node = (SectionNode) SkriptLogger.getNode();
        if (node == null)
            throw new UnsupportedOperationException();

        final BotEntity entity = BotFactory.build(node, name);

        this.entity = entity == null ? BotEntity.empty(name) : entity;
        nukeSectionNode(node);
        return entity != null;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "define bot named " + entity.getName();
    }

    public void nukeSectionNode(SectionNode sectionNode) {
        List<Node> nodes = new ArrayList<>();
        for (Node node : sectionNode) {
            nodes.add(node);
        }
        for (Node n : nodes) {
            sectionNode.remove(n);
        }
    }
}
