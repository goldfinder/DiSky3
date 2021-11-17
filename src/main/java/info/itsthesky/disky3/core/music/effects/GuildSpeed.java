package info.itsthesky.disky3.core.music.effects;

import com.github.natanbc.lavadsp.timescale.TimescalePcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.filter.UniversalPcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky3.api.music.EffectData;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class GuildSpeed extends AudioEffectExpression<TimescalePcmAudioFilter> {

    static {
        register(GuildSpeed.class, "speed");
    }

    @Override
    public Number parse(EffectData data) {
        return data.getSpeed();
    }

    @Override
    public TimescalePcmAudioFilter create(AudioTrack track, AudioDataFormat format, UniversalPcmAudioFilter output) {
        return new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
    }

    @Override
    public void setInternalData(TimescalePcmAudioFilter data, Number value) {
        data.setSpeed(value.doubleValue());
    }

    @Override
    public Consumer<EffectData> setEffectData(Number value) {
        return data -> data.setSpeed(value.doubleValue());
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "track speed";
    }
}
