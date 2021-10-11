package info.itsthesky.disky3.core.skript.properties.guild;

import info.itsthesky.disky3.api.skript.properties.MultipleGuildProperty;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

public class GuildMembers extends MultipleGuildProperty<Member> {

    static {
        register(
                GuildMembers.class,
                Member.class,
                "[discord] [guild] member[s]",
                "guild"
        );
    }

    @Override
    public @NotNull Member[] converting(Guild guild) {
        return guild.getMembers().toArray(new Member[0]);
    }

    @Override
    public @NotNull Class<? extends Member> getReturnType() {
        return Member.class;
    }
}
