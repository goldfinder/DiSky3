package info.itsthesky.disky3.api.skript.parser;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.command.Commands;
import ch.njol.skript.config.Config;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.TriggerSection;
import ch.njol.skript.log.LogEntry;
import ch.njol.skript.log.RetainingLogHandler;
import ch.njol.skript.log.SkriptLogger;
import info.itsthesky.disky3.api.ReflectionUtils;
import info.itsthesky.disky3.api.skript.adapter.SkriptAdapter;
import info.itsthesky.disky3.core.commands.Argument;
import info.itsthesky.disky3.core.commands.CommandFactory;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ParserUtils {

    public static class ParserData {

        private final List<Argument<?>> discordArgs;
        private final List<ch.njol.skript.command.Argument<?>> cmdArgs;
        private final Config script;

        public ParserData() {
            this(CommandFactory.currentArguments, Commands.currentArguments, SkriptLogger.getNode().getConfig());
        }

        public ParserData(List<Argument<?>> discordArgs, List<ch.njol.skript.command.Argument<?>> cmdArgs, Config script) {
            this.discordArgs = discordArgs;
            this.cmdArgs = cmdArgs;
            this.script = script;
        }

        public List<Argument<?>> getDiscordArgs() {
            return discordArgs;
        }

        public List<ch.njol.skript.command.Argument<?>> getCmdArgs() {
            return cmdArgs;
        }

        public Config getScript() {
            return script;
        }
    }

    public static List<String> parse(String code, Event e, boolean shouldExecute, ParserData data) {

        final RetainingLogHandler log = SkriptLogger.startRetainingLog();
        try {

            Commands.currentArguments = data.getCmdArgs();
            CommandFactory.currentArguments = data.getDiscordArgs();

            code = code
                    .replaceAll("\\\\n(?=[^\"]*(?:\"[^\"]*\"[^\"]*)*$)", "\n")
                    .replaceAll("\\\\t(?=[^\"]*(?:\"[^\"]*\"[^\"]*)*$)", "\t")
            ;

            Config c = new Config(code, "DiSky/evaluate.sk", true, false, ":");
            ReflectionUtils.setField(c.getClass(), c, "file", new File("DiSky/evaluate.sk").toPath());
            SkriptAdapter.getInstance().setCurrentScript(data.getScript());
            ScriptLoader.setCurrentEvent("evaluate effect", e.getClass());

            TriggerSection ts = new TriggerSection(c.getMainNode()) {
                @Override
                protected TriggerItem walk(@NotNull Event event) {
                    return walk(event, true);
                }

                @Override
                public String toString(Event event, boolean b) {
                    return "evaluate effect";
                }
            };

            ScriptLoader.deleteCurrentEvent();
            SkriptAdapter.getInstance().setCurrentScript(null);
            Commands.currentArguments = null;
            CommandFactory.currentArguments = null;
            if (shouldExecute)
                TriggerItem.walk(ts, e);

            return log.getErrors().stream().map(LogEntry::getMessage).map(msg -> msg.replaceAll("DiSky/evaluate.sk, ", "")).collect(Collectors.toList());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            log.stop();
        }
        return new ArrayList<>();
    }

}
