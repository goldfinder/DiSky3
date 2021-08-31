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
import net.dv8tion.jda.api.utils.data.DataObject;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Embed Thumbnail")
@Description("Represent the Thumbnail of an embed. Must be a http link (external image).")
@Since("3.0")
@Examples("set thumbnail of embed to \"https://starwars-universe.com/images/actualites/the_mandalorian/thechild.jpg\"")
public class EmbedThumbnail extends SimplePropertyExpression<EmbedBuilder, String> {

    static {
        register(
                EmbedThumbnail.class,
                String.class,
                "[embed] thumbnail",
                "embedbuilder"
        );
    }

    private boolean useScope;
    private NodeInformation info;

    @Override
    protected String getPropertyName() {
        return "embed thumbnail";
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
        return builder.build().getThumbnail().getUrl();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET && delta.length != 0 && delta[0] != null) {

            final String value = delta[0].toString();
            final @Nullable EmbedBuilder embed = Utils.verifyVar(e, getExpr(), null);

            if (value == null || embed == null) return;

            try {

                embed.setThumbnail(value);

                if (useScope)
                    ScopeEmbed.lastEmbed.setThumbnail(value);

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
