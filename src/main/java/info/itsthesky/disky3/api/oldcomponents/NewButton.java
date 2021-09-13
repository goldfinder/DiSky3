package info.itsthesky.disky3.api.oldcomponents;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.emojis.Emote;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.Component;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NewButton extends SimpleExpression<Component> {

    static {
        Skript.registerExpression(
                NewButton.class,
                Component.class,
                ExpressionType.COMBINED,
                "[a] new [discord] button with [the] (id|url) %string% [with [the] [button] style %-buttonstyle%] [with [the] (text|label) %-string%] [with [the] [emoji] %-emote%]",
                "[a] new [discord] link button with [the] (id|url) %string% [with [the] [button] style %-buttonstyle%] [with [the] (text|label) %-string%] [with [the] [emoji] %-emote%]"
        );
    }

    private @NotNull Expression<String> exprID;
    private @Nullable Expression<ButtonStyle> exprStyle;
    private @Nullable Expression<String> exprText;
    private @Nullable Expression<Emote> exprEmote;

    private boolean isLink = false;

    @Override
    protected Component @NotNull [] get(@NotNull Event e) {

        @NotNull String id = Utils.verifyVar(e, exprID, null);
        @NotNull ButtonStyle style = Utils.verifyVar(e, exprStyle, ButtonStyle.PRIMARY);
        @Nullable String text = Utils.verifyVar(e, exprText, null);
        @Nullable Emote emote = Utils.verifyVar(e, exprEmote, null);

        if (id == null) return new Button[0];
        // Button must have either a text or an emote
        if (emote == null && text == null) return new Button[0];

        Button button = Button.of(
                (isLink ? ButtonStyle.LINK : style),
                id,
                (emote.isEmote() ? Emoji.fromEmote(emote.getEmote()) : Emoji.fromUnicode(emote.getMention()))
        );

        return new Component [] {button};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Component> getReturnType() {
        return Component.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "new components row";
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {

        exprID = (Expression<String>) exprs[0];
        exprStyle = (Expression<ButtonStyle>) exprs[1];
        exprText = (Expression<String>) exprs[2];
        exprEmote = (Expression<Emote>) exprs[3];

        isLink = matchedPattern == 1;

        return true;
    }

}
