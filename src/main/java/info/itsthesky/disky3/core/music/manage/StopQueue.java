package info.itsthesky.disky3.core.music.manage;

import info.itsthesky.disky3.api.music.AudioUtils;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

public class StopQueue extends AudioEffect{

    static {
        register(
                StopQueue.class,
                "stop [current] queue"
        );
    }

    @Override
    public String effectToString() {
        return "stop current queue";
    }

    @Override
    public void execute(@NotNull Guild guild) {
        AudioUtils.getGuildAudioPlayer(guild).getScheduler().clearQueue();
    }
}
