package info.itsthesky.disky3.core.skript.properties.embed;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.section.EffectSection;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Embed Footer Icon")
@Description("Represent the footer icon of embed. You CANNOT set an icon without a TEXT FOOTER!")
@Since("3.0")
@Examples("set footer icon of embed to \"https://www.ipreunion.com/thumb/source/jpg/0583542001585981837/w650-h365-q95-cc/volcan-de-phil-en-image.jpg\"")
public class EmbedFooterURL extends SimplePropertyExpression<EmbedBuilder, String> {

    static {
        register(
                EmbedFooterURL.class,
                String.class,
                "[embed] footer icon",
                "embedbuilder"
        );
    }

    private boolean useScope;
    private NodeInformation info;

    @Override
    protected @NotNull String getPropertyName() {
        return "embed footer icon";
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

            String previousFooterText = Utils.tryOrDie(
                    builder -> builder.build().getFooter().getText(),
                    embed,
                    null
            );

            try {

                if (previousFooterText == null)
                    throw new DiSkyException("You can't set a footer icon without a footer text!");

                embed.setFooter(previousFooterText, value);

                if (useScope)
                    ScopeEmbed.lastEmbed.setFooter(previousFooterText, value);

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
