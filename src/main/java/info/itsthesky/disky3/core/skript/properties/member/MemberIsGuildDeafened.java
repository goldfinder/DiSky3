package info.itsthesky.disky3.core.skript.properties.member;

import info.itsthesky.disky3.api.skript.PropertyCondition;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

public class MemberIsGuildDeafened extends PropertyCondition<Member> {

    static {
        register(
                MemberIsGuildDeafened.class,
                PropertyType.BE,
                "guild deafen[ed]",
                "member"
        );
    }

    @Override
    public boolean check(@NotNull Member member) {
        return member.getVoiceState().isGuildDeafened();
    }

    @Override
    protected String getPropertyName() {
        return "guild deafened";
    }
}
