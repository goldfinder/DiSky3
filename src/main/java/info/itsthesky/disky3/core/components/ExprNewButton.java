package info.itsthesky.disky3.core.components;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.emojis.Emote;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import org.bukkit.event.Event;

@Name("New Button")
@Description("Create a new button with an ID and some oehter options. It can be either enabled or disabled, and either link or action. If the button is a link-type, then the ID will be the URL that the user will be redirect to.")
public class ExprNewButton extends SimpleExpression<Button> {
    
    static {
        Skript.registerExpression(ExprNewButton.class, Button.class, ExpressionType.SIMPLE,
                "[a] new [(enabled|disabled)] [link] button [with (id|url)] %string%[,] (with style|styled) %buttonstyle%[,] [with (content|text) %-string%][,] [with [emoji] %-emote%]");
    }

    private Expression<String> exprIdOrURL;
    private Expression<ButtonStyle> exprStyle;
    private Expression<String> exprContent;
    private Expression<Emote> exprEmoji;
    private boolean isEnabled;
    private boolean isLink;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprIdOrURL = (Expression<String>) exprs[0];
        exprStyle = (Expression<ButtonStyle>) exprs[1];
        exprContent = (Expression<String>) exprs[2];
        exprEmoji = (Expression<Emote>) exprs[3];
        isEnabled = !parseResult.expr.contains("new disabled");
        isLink = parseResult.expr.contains("new link") ||
                parseResult.expr.contains("new enabled link") ||
                parseResult.expr.contains("new disabled link");
        return true;
    }

    @Override
    protected Button[] get(Event e) {
        String idOrURL = exprIdOrURL.getSingle(e);
        ButtonStyle style = exprStyle.getSingle(e);

        String content = exprContent == null ? null : exprContent.getSingle(e);
        Emote emoji = exprEmoji == null ? null : exprEmoji.getSingle(e);

        if (style == null || idOrURL == null) return new Button[0];
        if (emoji == null && content == null) return new Button[0];

        if (isLink)
            style = ButtonStyle.LINK;

        Button button = (content == null ? Button.of(style, idOrURL, "") : Button.of(style, idOrURL, content));

        if (emoji != null && content != null)
            button = Button.of(style, idOrURL, content).withEmoji(Utils.convert(emoji));

        if (!isEnabled)
            button = button.asDisabled();

        return new Button[] {button};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Button> getReturnType() {
        return Button.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "new button with id or url " + exprIdOrURL.toString(e, debug);
    }
}