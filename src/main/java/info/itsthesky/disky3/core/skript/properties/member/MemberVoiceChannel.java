package info.itsthesky.disky3.core.skript.properties.member;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.jetbrains.annotations.NotNull;

public class MemberVoiceChannel extends SimplePropertyExpression<Member, VoiceChannel> {

    static {
        register(
                MemberVoiceChannel.class,
                VoiceChannel.class,
                "[discord] voice[( |-)]channel",
                "member"
        );
    }

    private NodeInformation info;

    @Override
    public @NotNull VoiceChannel convert(Member member) {
        return member.getVoiceState().getChannel();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "discord online status";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        info = new NodeInformation();
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @NotNull Class<? extends VoiceChannel> getReturnType() {
        return VoiceChannel.class;
    }
}
