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
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.skript.EffectSection;
import info.itsthesky.disky3.api.skript.NodeInformation;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Embed Title URL")
@Description("Represent the title url of an embed. Keep in mind you need a title text set in order to add an URL!")
@Since("3.0")
@Examples("set title url of embed to \"https://www.youtube.com/watch?v=7JEqsCFrD_c\"")
public class EmbedTitleURL extends SimplePropertyExpression<EmbedBuilder, String> {

    static {
        register(
                EmbedTitleURL.class,
                String.class,
                "[embed] title url",
                "embedbuilder"
        );
    }

    private boolean useScope;
    private NodeInformation info;

    @Override
    protected String getPropertyName() {
        return "embed title url";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        useScope = EffectSection.isCurrentSection(ScopeEmbed.class);
        info = new NodeInformation();
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Nullable
    @Override
    public String convert(EmbedBuilder builder) {
        return builder.build().getUrl();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET && delta.length != 0 && delta[0] != null) {

            final String value = delta[0].toString();
            final @Nullable EmbedBuilder embed = Utils.verifyVar(e, getExpr(), null);

            if (value == null || embed == null) return;

            String previousTitle = null;
            if (!embed.isEmpty())
                previousTitle = embed.build().getTitle();

            try {

                embed.setTitle(previousTitle, value);

                if (useScope)
                    ScopeEmbed.lastEmbed.setTitle(previousTitle, value);

            } catch (Exception ex) {
                DiSky.exception(ex, info);
            }

        }
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET)
            return CollectionUtils.array(String.class);
        return CollectionUtils.array();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}
