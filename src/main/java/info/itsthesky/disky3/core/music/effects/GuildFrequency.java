package info.itsthesky.disky3.core.music.effects;

import com.github.natanbc.lavadsp.tremolo.TremoloPcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.filter.UniversalPcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky3.api.music.EffectData;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class GuildFrequency extends AudioEffectExpression<TremoloPcmAudioFilter> {

    static {
        register(GuildFrequency.class, "frequency");
    }

    @Override
    public Number parse(EffectData data) {
        return data.getDepth();
    }

    @Override
    public TremoloPcmAudioFilter create(AudioTrack track, AudioDataFormat format, UniversalPcmAudioFilter output) {
        return new TremoloPcmAudioFilter(output, format.channelCount, format.sampleRate);
    }

    @Override
    public void setInternalData(TremoloPcmAudioFilter data, Number value) {
        data.setFrequency(value.floatValue());
    }

    @Override
    public Consumer<EffectData> setEffectData(Number value) {
        return data -> data.setFrequency(value.doubleValue());
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "track depth";
    }

}
