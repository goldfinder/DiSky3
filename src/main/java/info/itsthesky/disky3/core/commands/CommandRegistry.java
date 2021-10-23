package info.itsthesky.disky3.core.commands;

import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.config.Config;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SelfRegisteringSkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Name("Discord Command")
@Description("Custom DiSky discord command system. Arguments works like the normal skript's one and accept both optional and require arguments.")
@Examples("discord command move <member> <voicechannel>:\n" +
        "\tprefixes: !\n" +
        "\ttrigger:\n" +
        "\t\treply with mention tag of arg-2\n" +
        "\t\tmove arg-1 to arg-2")
@Since("3.0")
public class CommandRegistry extends SelfRegisteringSkriptEvent {

    static {
        Skript.registerEvent("Discord Command", CommandRegistry.class, CommandEvent.class, "discord command <([^\\s]+)( .+)?$>");

        EventValues.registerEventValue(CommandEvent.class, CommandObject.class, new Getter<CommandObject, CommandEvent>() {
                    @Override
                    public CommandObject get(@NotNull CommandEvent event) {
                        return event.getCommand();
                    }
                }
                , 0);

        EventValues.registerEventValue(CommandEvent.class, User.class, new Getter<User, CommandEvent>() {
                    @Override
                    public User get(@NotNull CommandEvent event) {
                        return event.getUser();
                    }
                }
                , 0);


        EventValues.registerEventValue(CommandEvent.class, Member.class, new Getter<Member, CommandEvent>() {
                    @Override
                    public Member get(@NotNull CommandEvent event) {
                        return event.getMember();
                    }
                }
                , 0);

        EventValues.registerEventValue(CommandEvent.class, GuildChannel.class, new Getter<GuildChannel, CommandEvent>() {
                    @Override
                    public GuildChannel get(@NotNull CommandEvent event) {
                        return event.getTxtChannel();
                    }
                }
                , 0);


        EventValues.registerEventValue(CommandEvent.class, MessageChannel.class, new Getter<MessageChannel, CommandEvent>() {
                    @Override
                    public MessageChannel get(@NotNull CommandEvent event) {
                        return event.getMessageChannel();
                    }
                }
                , 0);

        EventValues.registerEventValue(CommandEvent.class, UpdatingMessage.class, new Getter<UpdatingMessage, CommandEvent>() {
                    @Override
                    public UpdatingMessage get(@NotNull CommandEvent event) {
                        return UpdatingMessage.from(event.getMessage());
                    }
                }
                , 0);

        EventValues.registerEventValue(CommandEvent.class, Guild.class, new Getter<Guild, CommandEvent>() {
                    @Override
                    public Guild get(@NotNull CommandEvent event) {
                        return event.getGuild();
                    }
                }
                , 0);

        EventValues.registerEventValue(CommandEvent.class, Bot.class, new Getter<Bot, CommandEvent>() {
            @Override
            public Bot get(@NotNull CommandEvent event) {
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
        ScriptLoader.setCurrentEvent("discord command", CommandEvent.class);

        CommandObject cmd = CommandFactory.getInstance().add(sectionNode);
        command = cmd == null ? command : cmd.getName();

        ScriptLoader.setCurrentEvent(originalName, originalEvents);
        SkriptAdapter.getInstance().setHasDelayedBefore(originalDelay);
        nukeSectionNode(sectionNode);

        return cmd != null;
    }

    @Override
    public void register(@NotNull Trigger t) {
    }

    @Override
    public void unregister(@NotNull Trigger t) {
        CommandFactory.getInstance().remove(command);
    }

    @Override
    public void unregisterAll() {
        CommandFactory.getInstance().commandMap.clear();
    }


    @Override
    public @NotNull String toString(final Event e, final boolean debug) {
        return "discord command " + command + (arguments == null ? "" : arguments);
    }

    public void nukeSectionNode(SectionNode sectionNode) {
        List<Node> nodes = new ArrayList<>();
        for (Iterator<Node> iterator = sectionNode.iterator(); iterator.hasNext(); ) {
            nodes.add(iterator.next());
        }
        for (Node n : nodes) {
            sectionNode.remove(n);
        }
    }

}