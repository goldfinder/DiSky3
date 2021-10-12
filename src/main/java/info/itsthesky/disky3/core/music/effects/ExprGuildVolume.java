package info.itsthesky.disky3.core.music.effects;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import info.itsthesky.disky3.api.music.AudioUtils;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Guild Volume")
@Description("Get or change the volume of a guild.")
@Examples("discord command volume [<number>]:\n" +
        "\tprefixes: *\n" +
        "\ttrigger:\n" +
        "\t\tif arg 1 is not set:\n" +
        "\t\t\treply with \":speaking_head: **Current __volume__: `%volume of event-guild%`**\"\n" +
        "\t\t\tstop\n" +
        "\t\tset {_old} to volume of event-guild\n" +
        "\t\tset volume of event-guild to arg-1\n" +
        "\t\treply with \":speaking_head: **The __volume__ has been changed: `%{_old}%` -> `%volume of event-guild%`**\"")
@Since("1.6-pre2")
public class ExprGuildVolume extends SimplePropertyExpression<Guild, Number> {

    static {
        register(ExprGuildVolume.class, Number.class,
                "[discord] [audio] [guild] volume",
                "guild"
        );
    }

    @Nullable
    @Override
    public Number convert(@NotNull Guild guild) {
        return AudioUtils.getGuildAudioPlayer(guild).getPlayer().getVolume();
    }

    @Override
    public @NotNull Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "guild volume";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (
                mode.equals(Changer.ChangeMode.ADD) ||
                        mode.equals(Changer.ChangeMode.REMOVE) ||
                        mode.equals(Changer.ChangeMode.SET)
        ) {
            return CollectionUtils.array(Number.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(@NotNull Event e, @Nullable Object[] delta, Changer.@NotNull ChangeMode mode) {
        if (delta == null || delta[0] == null || !(delta[0] instanceof Number)) return;
        Number value = (Number) delta[0];
        switch (mode) {
            case SET:
                for (Guild guild : getExpr().getArray(e)) {
                    AudioPlayer player = AudioUtils.getGuildAudioPlayer(guild).getPlayer();
                    player.setVolume(value.intValue());
                }
                break;
            case ADD:
                for (Guild guild : getExpr().getArray(e)) {
                    AudioPlayer player = AudioUtils.getGuildAudioPlayer(guild).getPlayer();
                    player.setVolume(
                            player.getVolume() +
                                    value.intValue()
                    );
                }
                break;
            case REMOVE:
                for (Guild guild : getExpr().getArray(e)) {
                    AudioPlayer player = AudioUtils.getGuildAudioPlayer(guild).getPlayer();
                    player.setVolume(
                            player.getVolume() -
                                    value.intValue()
                    );
                }
                break;
        }
    }
}