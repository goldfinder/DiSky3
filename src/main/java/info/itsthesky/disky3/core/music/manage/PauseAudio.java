package info.itsthesky.disky3.core.music.manage;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.music.AudioUtils;
import info.itsthesky.disky3.core.events.audio.TrackEvent;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

@Name("Pause Guild Audio")
@Description("Pause the current audio a guild is playing.")
@Examples("discord command pause:\n" +
        "\tprefixes: *\n" +
        "\ttrigger:\n" +
        "\t\tpause in event-guild")
public class PauseAudio extends AudioEffect {

    static {
        register(
                PauseAudio.class,
                "pause [the] [audio] [track]"
        );
    }

    @Override
    public String effectToString() {
        return "pause audio";
    }

    @Override
    public void execute(@NotNull Guild guild) {
        AudioUtils.getGuildAudioPlayer(guild).getPlayer().setPaused(true);
        Utils.sync(() -> Bukkit.getPluginManager().callEvent(new TrackEvent(TrackEvent.TrackState.PAUSE, guild.getJDA(), guild, AudioUtils.getGuildAudioPlayer(guild).getScheduler().getQueue().peek())));
    }

}
