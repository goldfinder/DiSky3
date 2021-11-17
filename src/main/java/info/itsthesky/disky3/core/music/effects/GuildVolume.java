package info.itsthesky.disky3.core.music.effects;

import ch.njol.skript.classes.Changer;
import ch.njol.util.coll.CollectionUtils;
import com.github.natanbc.lavadsp.tremolo.TremoloPcmAudioFilter;
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
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GuildVolume extends ChangeableSimplePropertyExpression<Guild, Number> {

    static {
        register(
                GuildVolume.class,
                Number.class,
                "[(audio|track)] volume",
                "guild"
        );
    }


    @Override
    protected @NotNull String getPropertyName() {
        return "track volume";
    }

    @Override
    public @Nullable Number convert(Guild guild) {
        return AudioUtils.getGuildAudioPlayer(guild).getPlayer().getVolume();
    }

    @Override
    public @NotNull Class<? extends Number> getReturnType() {
        return Number.class;
    }

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
            final AudioPlayer player = AudioUtils.getGuildAudioPlayer(guild).getPlayer();
            switch (mode) {
                case SET:
                    player.setVolume(value.intValue());
                    break;
                case ADD:
                    player.setVolume(player.getVolume() + value.intValue());
                    break;
                case REMOVE:
                    player.setVolume(player.getVolume() - value.intValue());
                    break;
            }
        }
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
