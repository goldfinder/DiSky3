package info.itsthesky.disky3.core.music.manage;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.util.coll.CollectionUtils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.changers.MultipleChangeablePropertyExpression;
import info.itsthesky.disky3.api.music.AudioUtils;
import info.itsthesky.disky3.api.skript.MultiplyPropertyExpression;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Guild Queue")
@Description("Return all tracks from a specific guild.")
@Examples("set {_tracks::*} to queue of event-guild")
@Since("1.6-pre2")
public class ExprBotQueue extends MultipleChangeablePropertyExpression<Guild, AudioTrack> {

    static {
        register(ExprBotQueue.class, AudioTrack.class,
                "[discord] [audio] queue",
                "guild"
        );
    }

    @Nullable
    @Override
    public AudioTrack[] convert(Guild guild) {
        return AudioUtils.getGuildAudioPlayer(guild).trackScheduler.getQueue().toArray(new AudioTrack[0]);
    }

    @Override
    public @NotNull Class<? extends AudioTrack> getReturnType() {
        return AudioTrack.class;
    }

    @Override
    protected String getPropertyName() {
        return "bot queue";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode, boolean b) {
        return CollectionUtils.array();
    }

    @Override
    public void change(@NotNull Event e, @Nullable Object[] delta, Bot bot, Changer.@NotNull ChangeMode mode) { }
}