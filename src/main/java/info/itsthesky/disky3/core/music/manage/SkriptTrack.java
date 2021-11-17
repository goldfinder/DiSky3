package info.itsthesky.disky3.core.music.manage;

import ch.njol.skript.lang.Expression;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky3.api.music.AudioUtils;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

public class SkriptTrack extends AudioWaiterEffect<AudioTrack> {

    static {
        register(
                SkriptTrack.class,
                "skip [current] track",
                "[new] track"
        );
    }

    @Override
    public String effectToString() {
        return "skip current track";
    }

    @Override
    public void execute(@NotNull Guild guild) {
        final AudioTrack track = AudioUtils.skipTrack(guild);
        restart(track);
    }
}
