package info.itsthesky.disky3.core.skript.properties.member;

import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.PropertyCondition;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

public class MemberIsOwner extends PropertyCondition<Member> {

    static {
        register(
                MemberIsOwner.class,
                PropertyType.BE,
                "[the] owner",
                "member"
        );
    }

    @Override
    public boolean check(@NotNull Member member) {
        return member.isOwner();
    }

    @Override
    protected String getPropertyName() {
        return "the owner";
    }
}
