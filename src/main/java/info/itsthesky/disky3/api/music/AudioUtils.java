package info.itsthesky.disky3.api.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeSearchProvider;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.*;
import info.itsthesky.disky3.api.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author ItsTheSky
 */
public class AudioUtils {

    public static HashMap<AudioPlayer, AudioData> MUSIC_DATA = new HashMap<>();
    public static AudioPlayerManager MANAGER;
    public static YoutubeAudioSourceManager YOUTUBE_MANAGER_SOURCE;
    public static final YoutubeSearchProvider YOUTUBE_MANAGER_SEARCH = new YoutubeSearchProvider();
    private static final SoundCloudAudioSourceManager SOUNDCLOUD_AUDIO_MANAGER = SoundCloudAudioSourceManager.createDefault();
    public static Map<String, GuildAudioManager> MUSIC_MANAGERS;
    private static final Map<Long, EffectData> GUILDS_EFFECTS = new HashMap<>();
    private final static DefaultAudioPlayerManager DEFAULT_MANAGER = new DefaultAudioPlayerManager();
    private final static LocalAudioSourceManager LOCAL_MANAGER = new LocalAudioSourceManager();

    public static @Nullable AudioTrack getCurrentTrack(Guild guild) {
        final @Nullable GuildAudioManager manager = MUSIC_MANAGERS.getOrDefault(guild.getId() + guild.getJDA().getSelfUser().getId(), null);
        if (manager == null)
            return null;
        return manager.getPlayer().getPlayingTrack();
    }

    public static EffectData getEffectData(Guild guild) {
        if (!GUILDS_EFFECTS.containsKey(guild.getIdLong())) {
            EffectData data = new EffectData(guild);
            GUILDS_EFFECTS.put(guild.getIdLong(), data);
            return data;
        } else {
            return GUILDS_EFFECTS.get(guild.getIdLong());
        }
    }

    public static void initializeAudio() {
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        playerManager.getConfiguration().setFilterHotSwapEnabled(true);
        MANAGER = playerManager;
        MUSIC_MANAGERS = new HashMap<>();
        AudioSourceManagers
                .registerRemoteSources(playerManager);
        AudioSourceManagers
                .registerLocalSource(playerManager);
        AudioSourceManagers
                .registerRemoteSources(DEFAULT_MANAGER);
        YOUTUBE_MANAGER_SOURCE = new YoutubeAudioSourceManager();
    }

    public static AudioTrack[] search(String url, AudioSite site)
    {
        final String trackUrl;

        //Strip <>'s that prevent discord from embedding link resources
        if (url.startsWith("<") && url.endsWith(">"))
            trackUrl = url.substring(1, url.length() - 1);
        else
            trackUrl = url;

        String siteKey = site.equals(AudioSite.SOUNDCLOUD) ? "scsearch:" : "ytsearch:";

        CompletableFuture<List<AudioTrack>> cf = new CompletableFuture<>();
        DEFAULT_MANAGER.loadItem((Utils.isURL(trackUrl) ? "" : siteKey) + trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                cf.complete(Collections.singletonList(track));
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                cf.complete(playlist.getTracks());
            }

            @Override
            public void noMatches() {
                cf.complete(new ArrayList<>());
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                cf.completeExceptionally(exception);
            }
        });
        return cf.join().toArray(new AudioTrack[0]);
    }

    @Nullable
    public static AudioTrack loadFromFile(final File path) {
        AudioItem item = LOCAL_MANAGER.loadItem(DEFAULT_MANAGER, new AudioReference(path.getPath(), ""));
        return item instanceof AudioTrack ? (AudioTrack) item : null;
    }

    public static synchronized GuildAudioManager getGuildAudioPlayer(Guild guild) {
        final String key = guild.getId().concat(guild.getJDA().getSelfUser().getId());
        GuildAudioManager musicManager = MUSIC_MANAGERS.get(key);

        if (musicManager == null) {
            musicManager = new GuildAudioManager(guild, guild.getJDA());
            MUSIC_MANAGERS.put(key, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }

    public static void play(Guild guild, AudioTrack... tracks) {
        GuildAudioManager musicManager = getGuildAudioPlayer(guild);
        //connectToVoiceChannel(guild.getAudioManager(), channel);
        for (AudioTrack track : tracks) {
            musicManager.trackScheduler.queue(track);
        }
    }

    public static void connectToVoiceChannel(AudioManager audioManager, VoiceChannel channel, boolean force) {
        if (force)
            audioManager.closeAudioConnection();
        if (!audioManager.isConnected())
            audioManager.openAudioConnection(channel);
    }

    public static AudioTrack skipTrack(Guild guild) {
        GuildAudioManager musicManager = getGuildAudioPlayer(guild);
        return musicManager.trackScheduler.nextTrack();
    }
}
