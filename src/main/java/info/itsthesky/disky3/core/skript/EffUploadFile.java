package info.itsthesky.disky3.core.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.AttachmentOption;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URL;

public class EffUploadFile extends WaiterEffect<UpdatingMessage> {

    static {
        Skript.registerEffect(
                EffUploadFile.class,
                "upload [the] [file] %string% [(named|with name) %-string%] [with [the] content %-embedbuilder/string/messagebuilder%] (to|in) [the] [channel] %channel/user/member% [(using|with) [bot] %-bot%] [with [attachments] options %-attachmentoptions%] [and store (it|the message) in %-object%]"
        );
    }

    private Expression<String> exprPath;
    private Expression<String> exprName;
    private Expression<Object> exprContent;
    private Expression<Object> exprTarget;
    private Expression<Bot> exprBot;
    private Expression<AttachmentOption> exprOptions;

    @Override
    @SuppressWarnings("unchecked")
    public boolean initEffect(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {

        exprPath = (Expression<String>) exprs[0];
        exprName = (Expression<String>) exprs[1];
        exprContent = (Expression<Object>) exprs[2];
        exprTarget = (Expression<Object>) exprs[3];
        exprBot = (Expression<Bot>) exprs[4];
        exprOptions = (Expression<AttachmentOption>) exprs[5];

        setChangedVariable((Variable<UpdatingMessage>) exprs[6]);

        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);

        if (exprBot == null) {
            Skript.error("The needed bot in the upload effect cannot be found!");
            return false;
        }

        return true;
    }

    @Override
    public void runEffect(Event e) {
        Object target = exprTarget.getSingle(e);
        String path = exprPath.getSingle(e);
        Bot bot = exprBot.getSingle(e);
        final AttachmentOption[] options = Utils.verifyVars(e, exprOptions, new AttachmentOption[0]);

        if (
                target == null ||
                        path == null ||
                        bot == null
        ) return;

        final @Nullable MessageBuilder builder = Utils.parseMessageContent(Utils.verifyVar(e, exprContent));

        final String fileName = Utils.verifyVar(e, exprName, getFromString(path));
        final InputStream fileStream;
        if (Utils.isURL(path)) {
            try {
                fileStream = new URL(path).openStream();
            } catch (IOException ioException) {
                DiSky.exception(ioException, getNode());
                return;
            }
        } else {
            final File fileFile = new File(path);
            if (fileFile == null || !fileFile.exists())
                return;
            try {
                fileStream = new FileInputStream(fileFile);
            } catch (FileNotFoundException ex) {
                DiSky.exception(ex, getNode());
                return;
            }
        }

        // Mean it's a text channel for sure
        if (target instanceof GuildChannel) {

            if (builder == null) {

                bot
                        .getCore()
                        .getTextChannelById(((TextChannel) target).getId())
                        .sendFile(fileStream, fileName, options)
                        .queue(msg -> restart(UpdatingMessage.from(msg)), ex -> DiSky.exception(ex, getNode()));

            } else {

                bot
                        .getCore()
                        .getTextChannelById(((TextChannel) target).getId())
                        .sendMessage(builder.build())
                        .addFile(fileStream, fileName, options)
                        .queue(msg -> restart(UpdatingMessage.from(msg)), ex -> DiSky.exception(ex, getNode()));

            }

        } else {
            // Mean it's a user / member

            if (builder == null) {

                bot.getCore().retrieveUserById(Utils.parseUser(target).getId()).queue(user -> {
                    user.openPrivateChannel().queue(txt -> txt
                            .sendFile(fileStream, fileName, options)
                            .queue(msg -> restart(UpdatingMessage.from(msg)), ex -> DiSky.exception(ex, getNode())));
                });

            } else {

                bot.getCore().retrieveUserById(Utils.parseUser(target).getId()).queue(user -> {
                    user.openPrivateChannel().queue(txt -> txt
                            .sendMessage(builder.build())
                            .addFile(fileStream, fileName, options)
                            .queue(msg -> restart(UpdatingMessage.from(msg)), ex -> DiSky.exception(ex, getNode())));
                });

            }
        }

    }

    private static String getFromString(String s) {
        if (Utils.isURL(s))
            return "file" + s.substring(s.lastIndexOf("."));
        return s;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "upload " + exprPath.toString(e, debug) + " to channel / user " + exprTarget.toString(e, debug);
    }
}
