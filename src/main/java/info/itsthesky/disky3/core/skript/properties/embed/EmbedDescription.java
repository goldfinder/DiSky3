package info.itsthesky.disky3.core.skript.properties.embed;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.section.EffectSection;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EmbedDescription extends SimplePropertyExpression<EmbedBuilder, String> {

    static {
        register(
                EmbedDescription.class,
                String.class,
                "[embed] desc[ription]",
                "embedbuilder"
        );
    }

    private boolean useScope;
    private NodeInformation info;

    @Override
    protected @NotNull String getPropertyName() {
        return "embed description";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        useScope = EffectSection.isCurrentSection(ScopeEmbed.class);
        info = new NodeInformation();
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Nullable
    @Override
    public String convert(EmbedBuilder builder) {
        return builder.build().getDescription();
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET && delta.length != 0 && delta[0] != null) {

            final String value = delta[0].toString();
            final @Nullable EmbedBuilder embed = Utils.verifyVar(e, getExpr(), null);

            if (value == null || embed == null) return;

            try {

                embed.setDescription(value);

                if (useScope)
                    ScopeEmbed.lastEmbed.setDescription(value);

            } catch (Exception ex) {
                DiSky.exception(ex, info);
            }

        }
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET)
            return CollectionUtils.array(String.class);
        return CollectionUtils.array();
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }
}
