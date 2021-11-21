package info.itsthesky.disky3.core.music.effects;

import com.github.natanbc.lavadsp.karaoke.KaraokePcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.filter.UniversalPcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky3.api.music.EffectData;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class GuildMono extends AudioEffectExpression<KaraokePcmAudioFilter> {

    static {
        register(
                GuildMono.class,
                "mono [level]"
        );
    }

    @Override
    public Number parse(EffectData data) {
        return data.getMono();
    }

    @Override
    public KaraokePcmAudioFilter create(AudioTrack track, AudioDataFormat format, UniversalPcmAudioFilter output) {
        return new KaraokePcmAudioFilter(output, format.channelCount, format.sampleRate);
    }

    @Override
    public void setInternalData(KaraokePcmAudioFilter data, Number value) {
        data.setMonoLevel(value.floatValue());
    }

    @Override
    public Consumer<EffectData> setEffectData(Number value) {
        return data -> data.setMono(value.floatValue());
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "mono level";
    }
}
