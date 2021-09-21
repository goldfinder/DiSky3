package info.itsthesky.disky3.core.skript.properties.member;

import info.itsthesky.disky3.api.skript.PropertyCondition;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

public class MemberIsGuildMuted extends PropertyCondition<Member> {

    static {
        register(
                MemberIsGuildMuted.class,
                PropertyType.BE,
                "guild mute[d]",
                "member"
        );
    }

    @Override
    public boolean check(@NotNull Member member) {
        return member.getVoiceState().isGuildMuted();
    }

    @Override
    protected String getPropertyName() {
        return "guild muted";
    }
}
