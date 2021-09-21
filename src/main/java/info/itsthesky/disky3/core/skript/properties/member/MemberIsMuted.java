package info.itsthesky.disky3.core.skript.properties.member;

import info.itsthesky.disky3.api.skript.PropertyCondition;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

public class MemberIsMuted extends PropertyCondition<Member> {

    static {
        register(
                MemberIsMuted.class,
                PropertyType.BE,
                "mute[d]",
                "member"
        );
    }

    @Override
    public boolean check(@NotNull Member member) {
        return member.getVoiceState().isMuted();
    }

    @Override
    protected String getPropertyName() {
        return "muted";
    }
}
