package info.itsthesky.disky3.core.music;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.music.AudioUtils;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.section.RestExceptionSection;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

@Name("Play Audio")
@Description("Play a specific audio track (can be get from search or load locale effects) to a voice channel")
@Examples("discord command play [<string>]:\n" +
        "\tprefixes: *\n" +
        "\taliases: p\n" +
        "\ttrigger:\n" +
        "\t\tif arg 1 is not set:\n" +
        "\t\t\treply with \":x: **You __must__ specify an URL or a YouTube input!**\"\n" +
        "\t\t\tstop\n" +
        "\t\tif voice channel of event-member is not set:\n" +
        "\t\t\treply with \":x: **You __must__ be in a voice channel in order to join you!**\"\n" +
        "\t\t\tstop\n" +
        "\t\t\n" +
        "\t\t# Check if the event member who made the command is in the voice channel of the bot.\n" +
        "\t\tset {_bot} to self member of event-bot in event-guild\n" +
        "\t\tset {_force} to false\n" +
        "\t\tif voice channel of {_bot} is set:\n" +
        "\t\t\tdiscord id of voice channel of event-member is not discord id of voice channel of {_bot}\n" +
        "\t\t\treply with \":x: **Error, I'm already connected to another voice channel.**\"\n" +
        "\t\t\tstop\n" +
        "\t\t\n" +
        "\t\tconnect event-bot to voice channel of event-member\n" +
        "\t\t\n" +
        "\t\tsearch in youtube for arg-1 and store it in {_r::*}\n" +
        "\t\t# It mean it's a playlist and not a single track\n" +
        "\t\tif arg-1 contain \"list=\":\n" +
        "\t\t\tif {_r::*} is not set:\n" +
        "\t\t\t\treply with \":x: **Can't found that playlist!**\"\n" +
        "\t\t\t\tstop\n" +
        "\t\t\t# We play all tracks listed on the playlist inputted\n" +
        "\t\t\tplay {_r::*} in event-guild\n" +
        "\t\t\tmake embed:\n" +
        "\t\t\t\tset author of embed to \"Successfully added **%size of {_r::*}% tracks** to your queue!\"\n" +
        "\t\t\t\tset author icon of embed to avatar of event-member\n" +
        "\t\t\t\tset color of embed to orange\n" +
        "\t\t\t\tset author url of embed to arg-1\n" +
        "\t\t\treply with last embed\n" +
        "\t\telse:\n" +
        "\t\t\tif {_r::1} is not set:\n" +
        "\t\t\t\treply with \":x: **Can't found anything for the input '%arg-1%'!**\"\n" +
        "\t\t\t\tstop\n" +
        "\t\t\tplay {_r::1} in event-guild\n" +
        "\t\t\tset {_track} to {_r::1}\n" +
        "\t\t\tmake embed:\n" +
        "\t\t\t\tset title of embed to \"%{_track}%\"\n" +
        "\t\t\t\tset title url of embed to track url of {_track}\n" +
        "\t\t\t\tset color of embed to lime\n" +
        "\t\t\t\tset footer of embed to \"Executed by %discord name of event-member%\"\n" +
        "\t\t\t\tset thumbnail of embed to track thumbnail of {_track}\n" +
        "\t\t\t\tadd \"`•` Duration: %track duration of {_track}%\" to {_l::*}\n" +
        "\t\t\t\tadd \"`•` Author: %track author of {_track}%\" to {_l::*}\n" +
        "\t\t\t\tset description of embed to join {_l::*} with nl\n" +
        "\t\t\treply with last embed")
@Since("1.6, 1.9 (rework using search), 2.0 (rework using connect effect)")
public class EffPlayAudio extends RestExceptionSection<Void> {

    static {

        register(EffPlayAudio.class, // [the] [bot] [(named|with name)] %string%
                "play [tracks] %tracks% in [the] [guild] %guild% [with %-bot%]");
    }

    private Expression<AudioTrack> exprTracks;
    private Expression<Guild> exprGuild;
    private Expression<Bot> exprBot;

    @Override
    public RestAction<Void> runRestAction(Event e) {
        AudioTrack[] tracks = exprTracks.getAll(e);
        Guild guild = exprGuild.getSingle(e);
        Bot bot = Utils.verifyVar(e, exprBot);
        if (guild == null || tracks.length == 0) return null;

        if (bot != null)
            guild = bot.getCore().getGuildById(guild.getId());

        AudioUtils.play(guild, tracks);
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprTracks = (Expression<AudioTrack>) exprs[0];
        exprGuild = (Expression<Guild>) exprs[1];
        exprBot = (Expression<Bot>) exprs[2];
        return true;
    }

    @Override
    public @NotNull String toString(Event e, boolean debug) {
        return "play tracks " + exprTracks.toString(e, debug) + " in guild " + exprGuild.toString(e, debug) + (exprBot == null ? "" : " with bot" + exprBot.toString(e, debug));
    }

}
