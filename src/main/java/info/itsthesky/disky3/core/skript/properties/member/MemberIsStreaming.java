package info.itsthesky.disky3.core.skript.properties.member;

import info.itsthesky.disky3.api.skript.PropertyCondition;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

public class MemberIsStreaming extends PropertyCondition<Member> {

    static {
        register(
                MemberIsStreaming.class,
                PropertyType.BE,
                "stream[ing]",
                "member"
        );
    }

    @Override
    public boolean check(@NotNull Member member) {
        return member.getVoiceState().isStream();
    }

    @Override
    protected String getPropertyName() {
        return "streaming";
    }
}
