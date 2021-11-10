package info.itsthesky.disky3.core.skript.slashcommand;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SelfRegisteringSkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SlashRegistry extends SelfRegisteringSkriptEvent {

    static {
        Skript.registerEvent("Slash Command", SlashRegistry.class, SlashEvent.class, "slash command <([^\\s]+)( .+)?$>");

        EventValues.registerEventValue(SlashEvent.class, SlashObject.class, new Getter<SlashObject, SlashEvent>() {
                    @Override
                    public SlashObject get(@NotNull SlashEvent event) {
                        return event.getCommand();
                    }
                }
                , 0);

        EventValues.registerEventValue(SlashEvent.class, User.class, new Getter<User, SlashEvent>() {
                    @Override
                    public User get(@NotNull SlashEvent event) {
                        return event.getUser();
                    }
                }
                , 0);


        EventValues.registerEventValue(SlashEvent.class, Member.class, new Getter<Member, SlashEvent>() {
                    @Override
                    public Member get(@NotNull SlashEvent event) {
                        return event.getMember();
                    }
                }
                , 0);

        EventValues.registerEventValue(SlashEvent.class, GuildChannel.class, new Getter<GuildChannel, SlashEvent>() {
                    @Override
                    public GuildChannel get(@NotNull SlashEvent event) {
                        return event.getChannel();
                    }
                }
                , 0);


        EventValues.registerEventValue(SlashEvent.class, MessageChannel.class, new Getter<MessageChannel, SlashEvent>() {
                    @Override
                    public MessageChannel get(@NotNull SlashEvent event) {
                        return event.getMessagechannel();
                    }
                }
                , 0);

        EventValues.registerEventValue(SlashEvent.class, Guild.class, new Getter<Guild, SlashEvent>() {
                    @Override
                    public Guild get(@NotNull SlashEvent event) {
                        return event.getGuild();
                    }
                }
                , 0);

        EventValues.registerEventValue(SlashEvent.class, Bot.class, new Getter<Bot, SlashEvent>() {
            @Override
            public Bot get(@NotNull SlashEvent event) {
                return BotManager.searchFromJDA(event.getBot());
            }
        }, 0);
    }

    private String arguments;
    private String command;

    @Override
    public boolean init(final Literal<?> @NotNull [] args, final int matchedPattern, final ParseResult parser) {
        command = parser.regexes.get(0).group(1);
        arguments = parser.regexes.get(0).group(2);
        SectionNode sectionNode = (SectionNode) SkriptLogger.getNode();

        String originalName = ScriptLoader.getCurrentEventName();
        Class<? extends Event>[] originalEvents = SkriptAdapter.getInstance().getCurrentEvents();
        Kleenean originalDelay = SkriptAdapter.getInstance().getHasDelayedBefore();
        ScriptLoader.setCurrentEvent("slash command", SlashEvent.class);

        boolean passed = SlashFactory.getInstance().add(sectionNode);

        ScriptLoader.setCurrentEvent(originalName, originalEvents);
        SkriptAdapter.getInstance().setHasDelayedBefore(originalDelay);
        nukeSectionNode(sectionNode);

        return passed;
    }

    @Override
    public void register(@NotNull Trigger t) { }

    @Override
    public void unregister(@NotNull Trigger t) {
        DiSky.debug("Removing command " + command);
        if (SlashObject.get(command) == null)
            return;
        SlashObject.get(command).delete();
    }

    @Override
    public void unregisterAll() { }

    @Override
    public @NotNull String toString(final Event e, final boolean debug) {
        return "slash command " + command + (arguments == null ? "" : arguments);
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