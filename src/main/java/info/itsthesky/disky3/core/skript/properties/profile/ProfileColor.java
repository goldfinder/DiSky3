package info.itsthesky.disky3.core.skript.properties.profile;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.SkriptColor;
import info.itsthesky.disky3.api.Utils;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Profile Color")
@Description({"Get the profile color accent. If the user have a custom banner, this will return none.",
"Use the 'profile banner' expression to get the avatar URL instead of the color accent in that case!"})
public class ProfileColor extends SimplePropertyExpression<User.Profile, Color> {

    static {
        register(
                ProfileColor.class,
                Color.class,
                "profile color [accent]",
                "userprofile"
        );
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "profile color";
    }

    @Override
    public @Nullable Color convert(User.Profile profile) {
        return Utils.convert(new java.awt.Color(profile.getAccentColorRaw()));
    }

    @Override
    public @NotNull Class<? extends Color> getReturnType() {
        return Color.class;
    }
}
