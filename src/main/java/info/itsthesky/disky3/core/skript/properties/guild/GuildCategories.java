package info.itsthesky.disky3.core.skript.properties.guild;

import info.itsthesky.disky3.api.skript.properties.MultipleGuildProperty;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Category;
import org.jetbrains.annotations.NotNull;

public class GuildCategories extends MultipleGuildProperty<Category> {

    static {
        register(
                GuildCategories.class,
                Category.class,
                "[discord] categories",
                "guild"
        );
    }

    @Override
    public @NotNull Class<? extends Category> getReturnType() {
        return Category.class;
    }

    @Override
    public @NotNull Category[] converting(Guild guild) {
        return guild.getCategories().toArray(new Category[0]);
    }

}
