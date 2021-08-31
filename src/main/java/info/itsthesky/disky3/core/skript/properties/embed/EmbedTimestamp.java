package info.itsthesky.disky3.core.skript.properties.embed;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.Date;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.skript.EffectSection;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Name("Embed Timestamp")
@Description("Represent the timestamp of an embed, it's the date wrote next to the footer text.")
@Since("3.0")
@Examples("set timestamp of embed to now")
public class EmbedTimestamp extends SimplePropertyExpression<EmbedBuilder, Date> {

    static {
        register(
                EmbedTimestamp.class,
                Date.class,
                "[embed] timestamp",
                "embedbuilder"
        );
    }

    private boolean useScope;
    private NodeInformation info;

    @Override
    protected @NotNull String getPropertyName() {
        return "embed timestamp";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        useScope = EffectSection.isCurrentSection(ScopeEmbed.class);
        info = new NodeInformation();
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @NotNull Date convert(EmbedBuilder builder) {
        return builder.isEmpty() ? null : new Date(builder.build().getTimestamp().getNano());
    }

    @Override
    public void change(@NotNull Event e, @Nullable Object[] delta, Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET && delta.length != 0 && delta[0] != null) {

            final Date value = (Date) delta[0];
            final @Nullable EmbedBuilder embed = Utils.verifyVar(e, getExpr(), null);

            if (embed == null) return;

            try {

                embed.setTimestamp(Instant.ofEpochMilli(value.getTimestamp()));

                if (useScope)
                    ScopeEmbed.lastEmbed.setTimestamp(Instant.ofEpochMilli(value.getTimestamp()));

            } catch (Exception ex) {
                DiSky.exception(ex, info);
            }

        }
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET)
            return CollectionUtils.array(
                    Date.class
            );
        return CollectionUtils.array();
    }

    @Override
    public @NotNull Class<? extends Date> getReturnType() {
        return Date.class;
    }
}
