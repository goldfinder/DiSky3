package info.itsthesky.disky3.core.skript.properties.guild;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.skript.NodeInformation;
import info.itsthesky.disky3.api.skript.properties.GuildProperty;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

public class GuildID extends GuildProperty<String> {

    static {
        register(
                GuildID.class,
                String.class,
                "[discord] id",
                "guild"
        );
    }

    @Override
    public @NotNull String converting(Guild guild) {
        return guild.getId();
    }

}
