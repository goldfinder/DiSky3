package info.itsthesky.disky3.core.skript.properties.member;

import info.itsthesky.disky3.api.skript.PropertyCondition;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

public class MemberIsSendingVideo extends PropertyCondition<Member> {

    static {
        register(
                MemberIsSendingVideo.class,
                PropertyType.BE,
                "send[ing] [a] video",
                "member"
        );
    }

    @Override
    public boolean check(@NotNull Member member) {
        return member.getVoiceState().isSendingVideo();
    }

    @Override
    protected String getPropertyName() {
        return "sending video";
    }
}
