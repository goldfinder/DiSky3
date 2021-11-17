package info.itsthesky.disky3.core.skript.properties.profile;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import info.itsthesky.disky3.api.skript.PropertyCondition;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

@Name("Profile Has Banner")
@Description({"Check if the specified profile have a custom banner set or not.",
        "Useful to manage either its banner ULR of color accent."})
public class HasBanner extends PropertyCondition<User.Profile> {

    static {
        register(
                HasBanner.class,
                PropertyType.HAVE,
                "[custom] banner",
                "userprofile"
        );
    }

    @Override
    public boolean check(User.@NotNull Profile profile) {
        return isNegated() == (profile.getBannerUrl() == null);
    }

    @Override
    protected String getPropertyName() {
        return "custom banner";
    }
}
