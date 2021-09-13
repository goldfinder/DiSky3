package info.itsthesky.disky3.core.skript.properties.guild;

import ch.njol.skript.util.Date;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.skript.properties.GuildProperty;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.TimeZone;

public class GuildAge extends GuildProperty<Date> {

    static {
        register(
                GuildAge.class,
                Date.class,
                "[discord] creat(ion|e[d]) (date|age|time)",
                "guild"
        );
    }

    @Override
    public @NotNull Date converting(Guild guild) {
        return Utils.convert(guild.getTimeCreated());
    }

}
