package info.itsthesky.disky3.core.events.other;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky3.api.skript.events.BukkitEvent;
import info.itsthesky.disky3.api.skript.events.DiSkyEvent;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.core.commands.CommandObject;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiSkyCommand extends SkriptEvent {

    static {
        DiSkyEvent.register("Disky Command", DiSkyCommand.class, EvtDiSkyCommand.class,
                "disky command")
                .setName("Disky Command")
                .setDesc("Fired when a disky/discord command is executed.")
                .setExample("on disky command:");

        EventValues.registerEventValue(EvtDiSkyCommand.class, CommandObject.class, new Getter<CommandObject, EvtDiSkyCommand>() {
            @Override
            public CommandObject get(@NotNull EvtDiSkyCommand event) {
                return event.command;
            }
        }, 0);

        EventValues.registerEventValue(EvtDiSkyCommand.class, UpdatingMessage.class, new Getter<UpdatingMessage, EvtDiSkyCommand>() {
            @Override
            public UpdatingMessage get(@NotNull EvtDiSkyCommand event) {
                return UpdatingMessage.from(event.jdaEvent.getMessage());
            }
        }, 0);

        EventValues.registerEventValue(EvtDiSkyCommand.class, User.class, new Getter<User, EvtDiSkyCommand>() {
            @Override
            public User get(@NotNull EvtDiSkyCommand event) {
                return event.jdaEvent.getAuthor();
            }
        }, 0);

        EventValues.registerEventValue(EvtDiSkyCommand.class, Member.class, new Getter<Member, EvtDiSkyCommand>() {
            @Override
            public Member get(@NotNull EvtDiSkyCommand event) {
                return event.jdaEvent.getMember();
            }
        }, 0);

        EventValues.registerEventValue(EvtDiSkyCommand.class, TextChannel.class, new Getter<TextChannel, EvtDiSkyCommand>() {
            @Override
            public TextChannel get(@NotNull EvtDiSkyCommand event) {
                return (TextChannel) event.jdaEvent.getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtDiSkyCommand.class, Guild.class, new Getter<Guild, EvtDiSkyCommand>() {
            @Override
            public Guild get(@NotNull EvtDiSkyCommand event) {
                return event.jdaEvent.getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtDiSkyCommand.class, Bot.class, new Getter<Bot, EvtDiSkyCommand>() {
            @Override
            public Bot get(@NotNull EvtDiSkyCommand event) {
                return BotManager.searchFromJDA(event.jdaEvent.getJDA());
            }
        }, 0);
    }

    @Override
    public boolean init(Literal<?> @NotNull [] exprs, int i, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(@NotNull Event event) {
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "on disky command";
    }

    public static class EvtDiSkyCommand extends BukkitEvent implements Cancellable {
        private final CommandObject command;
        private final MessageReceivedEvent jdaEvent;
        public EvtDiSkyCommand(CommandObject command,
                               MessageReceivedEvent e) {
            this.command = command;
            jdaEvent = e;
        }

        private boolean isCancelled = false;

        @Override
        public boolean isCancelled() {
            return isCancelled;
        }

        @Override
        public void setCancelled(boolean cancel) {
            isCancelled = cancel;
        }
    }
}