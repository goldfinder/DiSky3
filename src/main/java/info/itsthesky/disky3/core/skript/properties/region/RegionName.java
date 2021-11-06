package info.itsthesky.disky3.core.skript.properties.region;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import net.dv8tion.jda.api.Region;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Region Name")
@Description({"A more user-friendly way to show Region on Discord.",
        "It return the region name, formatted without underscore and upper case letters."})
public class RegionName extends SimplePropertyExpression<Region, String> {

    static {
        register(
                RegionName.class,
                String.class,
                "region name",
                "voiceregion"
        );
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "name";
    }

    @Override
    public @Nullable String convert(@NotNull Region region) {
        return region.getName();
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }
}
