package info.itsthesky.disky3.core.music.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.util.coll.CollectionUtils;
import com.github.natanbc.lavadsp.timescale.TimescalePcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.filter.FloatPcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.filter.UniversalPcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.changers.ChangeableSimplePropertyExpression;
import info.itsthesky.disky3.api.music.AudioUtils;
import info.itsthesky.disky3.api.music.EffectData;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class AudioEffectExpression<T extends FloatPcmAudioFilter> extends ChangeableSimplePropertyExpression<Guild, Number> {

    public static void register(final Class<? extends AudioEffectExpression<?>> c, final String property) {
        Skript.registerExpression(c, Number.class, ExpressionType.PROPERTY, "[the] (audio|track) " + property + " of %" + "guild" + "%", "%" + "guild" + "%'[s] (audio|track) " + property);
    }

    @Override
    public @NotNull Class<? extends Number> getReturnType() {
        return Number.class;
    }

    public abstract Number parse(EffectData data);

    private boolean customFilter() {
        return true;
    }

    public abstract T create(AudioTrack track, AudioDataFormat format, UniversalPcmAudioFilter output);

    @Override
    public void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode) {
        if (delta == null || delta[0] == null) return;
        Number value = (Number) delta[0];
        final List<Guild> guilds = bot == null ?
                Arrays.asList(getExpr().getArray(e)) :
                Arrays.stream(getExpr().getArray(e))
                        .map(guild -> bot.getCore().getGuildById(guild.getIdLong()))
                        .collect(Collectors.toList());
        for (Guild guild : guilds) {
            AudioPlayer player = AudioUtils.getGuildAudioPlayer(guild).getPlayer();
            player.setFilterFactory((track, format, output)-> {
                T filter = create(track, format, output);

                switch (mode) {
                    case SET:
                        setInternalData(filter, value);
                        setEffectData(value).accept(AudioUtils.getEffectData(guild));
                        break;
                    case ADD:
                        setInternalData(filter, convert(guild).doubleValue() + value.doubleValue());
                        setEffectData(convert(guild).doubleValue() + value.doubleValue()).accept(AudioUtils.getEffectData(guild));
                        break;
                    case REMOVE:
                        setInternalData(filter, convert(guild).doubleValue() - value.doubleValue());
                        setEffectData(convert(guild).doubleValue() - value.doubleValue()).accept(AudioUtils.getEffectData(guild));
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

                return Collections.singletonList(filter);
            });
        }
    }

    public abstract void setInternalData(T data, Number value);

    public abstract Consumer<EffectData> setEffectData(Number value);

    @Override
    public @Nullable Number convert(Guild guild) {
        return parse(AudioUtils.getEffectData(guild));
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean diskyChanger) {
        if (mode.equals(Changer.ChangeMode.ADD) ||
                mode.equals(Changer.ChangeMode.REMOVE) ||
                mode.equals(Changer.ChangeMode.SET)) {
            return CollectionUtils.array(Number.class);
        }
        return CollectionUtils.array();
    }
}
