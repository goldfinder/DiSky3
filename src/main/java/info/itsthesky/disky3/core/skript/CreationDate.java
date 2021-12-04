package info.itsthesky.disky3.core.skript;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Date;
import info.itsthesky.disky3.api.Utils;
import net.dv8tion.jda.api.entities.ISnowflake;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Creation Date")
@Description({"Get the creation date (as Skript date) of any ISnowFlake entity, including, but not limited to:",
        "- Member",
        "- User",
        "- Role",
        "- Guild",
        "- Channel",
        "- etc...",
})
@Examples({"creation date of event-user",
        "created date of event-member"})
public class CreationDate extends SimplePropertyExpression<ISnowflake, Date> {

    static {
        register(
                CreationDate.class,
                Date.class,
                "[the] creat(ion|ed) date",
                "guild/member/user/role/channel"
        );
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "creation date";
    }

    @Override
    public @Nullable Date convert(ISnowflake o) {
        return Utils.convert(o.getTimeCreated());
    }

    @Override
    public @NotNull Class<? extends Date> getReturnType() {
        return Date.class;
    }

}
