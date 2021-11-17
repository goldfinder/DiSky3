package info.itsthesky.disky3.core.music.manage;

import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.music.AudioUtils;
import info.itsthesky.disky3.core.events.audio.TrackEvent;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class ResumeAudio extends AudioEffect {

    static {
        register(
                ResumeAudio.class,
                "resume [the] [audio] [track]"
        );
    }

    @Override
    public String effectToString() {
        return "resume audio track";
    }

    @Override
    public void execute(@NotNull Guild guild) {
        AudioUtils.getGuildAudioPlayer(guild).getPlayer().setPaused(false);
        Utils.sync(() -> Bukkit.getPluginManager().callEvent(new TrackEvent(TrackEvent.TrackState.PLAY, guild.getJDA(), guild, AudioUtils.getGuildAudioPlayer(guild).getScheduler().getQueue().peek())));
    }
}
