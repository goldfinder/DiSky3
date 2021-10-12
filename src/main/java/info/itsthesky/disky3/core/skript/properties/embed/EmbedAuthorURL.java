package info.itsthesky.disky3.core.skript.properties.embed;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
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

@Name("Embed Author URL")
@Description("Represent the author url of embed. You can't set the that without having an author text!")
@Since("3.0")
public class EmbedAuthorURL extends SimplePropertyExpression<EmbedBuilder, String> {

    static {
        register(
                EmbedAuthorURL.class,
                String.class,
                "[embed] author url",
                "embedbuilder"
        );
    }

    private boolean useScope;
    private NodeInformation info;

    @Override
    protected @NotNull String getPropertyName() {
        return "embed author url";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        useScope = EffectSection.isCurrentSection(ScopeEmbed.class);
        info = new NodeInformation();
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @NotNull String convert(EmbedBuilder builder) {
        return builder.isEmpty() ? null : builder.build().getAuthor().getName();
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET && delta.length != 0 && delta[0] != null) {

            final String value = (String) delta[0];
            final @Nullable EmbedBuilder embed = Utils.verifyVar(e, getExpr(), null);

            if (embed == null) return;

            String previousAuthorText = Utils.tryOrDie(
                    builder -> builder.build().getAuthor().getName(),
                    embed,
                    null
            );

            String previousAuthorIcon = Utils.tryOrDie(
                    builder -> builder.build().getAuthor().getIconUrl(),
                    embed,
                    null
            );

            try {

                if (previousAuthorText == null)
                    throw new DiSkyException("You can't set an author URL icon without an author text!");

                embed.setAuthor(previousAuthorText, value, previousAuthorIcon);

                if (useScope)
                    ScopeEmbed.lastEmbed.setAuthor(previousAuthorText, value, previousAuthorIcon);

            } catch (Exception ex) {
                DiSky.exception(ex, info);
            }

        }
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
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
