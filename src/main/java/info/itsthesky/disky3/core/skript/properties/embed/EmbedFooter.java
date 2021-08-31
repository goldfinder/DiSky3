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

@Name("Embed Footer")
@Description("Represent the footer text of embed.")
@Since("3.0")
@Examples("set footer of embed to \"Hello world /!\\\"")
public class EmbedFooter extends SimplePropertyExpression<EmbedBuilder, String> {

    static {
        register(
                EmbedFooter.class,
                String.class,
                "[embed] footer",
                "embedbuilder"
        );
    }

    private boolean useScope;
    private NodeInformation info;

    @Override
    protected @NotNull String getPropertyName() {
        return "embed footer";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        useScope = EffectSection.isCurrentSection(ScopeEmbed.class);
        info = new NodeInformation();
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @NotNull String convert(EmbedBuilder builder) {
        return builder.isEmpty() ? null : builder.build().getFooter().getText();
    }

    @Override
    public void change(@NotNull Event e, @Nullable Object[] delta, Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET && delta.length != 0 && delta[0] != null) {

            final String value = (String) delta[0];
            final @Nullable EmbedBuilder embed = Utils.verifyVar(e, getExpr(), null);

            if (embed == null) return;

            String previousFooterURL = Utils.tryOrDie(
                    builder -> builder.build().getFooter().getIconUrl(),
                    embed,
                    null
            );

            try {

                embed.setFooter(value, previousFooterURL);

                if (useScope)
                    ScopeEmbed.lastEmbed.setFooter(value, previousFooterURL);

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
                    String.class
            );
        return CollectionUtils.array();
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }
}
