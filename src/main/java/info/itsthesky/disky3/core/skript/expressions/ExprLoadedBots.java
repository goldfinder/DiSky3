package info.itsthesky.disky3.core.skript.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.skript.EasyExpression;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Name("All Loaded Bots")
@Description({"Get back every loaded bots on the server."})
@Examples("set {_b::*} to all loaded bots")
public class ExprLoadedBots extends EasyExpression<Bot> {

    static {
        register(
                ExprLoadedBots.class, Bot.class,
                "[(all|every)] load[ed] bot[s]"
        );
    }

    @Override
    protected List<Bot> convert() {
        return BotManager.getLoadedBots();
    }

    @Override
    protected String getSyntax() {
        return "all bots";
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends Bot> getReturnType() {
        return Bot.class;
    }
}
