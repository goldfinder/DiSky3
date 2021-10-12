package info.itsthesky.disky3.core.events.audio;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky3.api.skript.events.BukkitEvent;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

public class TrackEvent extends BukkitEvent {
    static {
        EventValues.registerEventValue(TrackEvent.class, Bot.class, new Getter<Bot, TrackEvent>() {
            @Override
            public Bot get(@NotNull TrackEvent event) {
                return BotManager.searchFromJDA(event.getJDA());
            }
        }, 0);

        EventValues.registerEventValue(TrackEvent.class, Guild.class, new Getter<Guild, TrackEvent>() {
            @Override
            public Guild get(@NotNull TrackEvent event) {
                return event.getGuild();
            }
        }, 0);

        EventValues.registerEventValue(TrackEvent.class, AudioTrack.class, new Getter<AudioTrack, TrackEvent>() {
            @Override
            public AudioTrack get(@NotNull TrackEvent event) {
                return event.getTrack();
            }
        }, 0);
    }

    private TrackState state;
    private JDA jda;
    private Guild guild;
    private AudioTrack track;

    public TrackEvent() {

    }

    public TrackEvent(TrackState state, JDA jda, Guild guild, AudioTrack track) {
        this.state = state;
        this.jda = jda;
        this.guild = guild;
        this.track = track;
    }

    public TrackState getState() {
        return state;
    }

    public JDA getJDA() {
        return jda;
    }

    public Guild getGuild() {
        return guild;
    }

    public AudioTrack getTrack() {
        return track;
    }

    public enum TrackState {

        PAUSE, PLAY, START, END, SEEK

    }

}