package info.itsthesky.disky3.core.music.effects;

import com.github.natanbc.lavadsp.rotation.RotationPcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.filter.UniversalPcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky3.api.music.EffectData;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class GuildRotation extends AudioEffectExpression<RotationPcmAudioFilter> {

    static {
        register(
                GuildRotation.class,
                "rotation [speed]"
        );
    }

    @Override
    public Number parse(EffectData data) {
        return data.getRotation();
    }

    @Override
    public RotationPcmAudioFilter create(AudioTrack track, AudioDataFormat format, UniversalPcmAudioFilter output) {
        return new RotationPcmAudioFilter(output, format.sampleRate);
    }

    @Override
    public void setInternalData(RotationPcmAudioFilter data, Number value) {
        data.setRotationSpeed(value.doubleValue());
    }

    @Override
    public Consumer<EffectData> setEffectData(Number value) {
        return data -> data.setRotation(value.doubleValue());
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "rotation speed";
    }
}
