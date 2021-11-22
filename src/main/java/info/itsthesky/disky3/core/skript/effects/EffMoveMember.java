package info.itsthesky.disky3.core.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.skript.WaiterEffect;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Move Member")
@Description({"Move the specified member to another audio channel.",
"You can only move member that are already connected to a channel, aka you cannot force a member to connect!"})
public class EffMoveMember extends WaiterEffect {

    static {
        Skript.registerEffect(
                EffMoveMember.class,
                "move [the] [member] %member% in [the] [(audio|stage|voice)] [channel] %voicechannel% [(with|using) [bot] %-bot%]"
        );
    }

    private Expression<Member> exprMember;
    private Expression<AudioChannel> exprChannel;
    private Expression<Bot> exprBot;

    @Override
    public void runEffect(@NotNull Event e) {
        Member instance = exprMember.getSingle(e);
        AudioChannel channelInstance = exprChannel.getSingle(e);
        final Bot bot = exprBot.getSingle(e);

        if (instance == null || bot == null) {
            restart();
            return;
        }

        bot
                .getCore()
                .getGuildById(instance.getGuild().getId())
                .retrieveMemberById(instance.getId())
                .queue(member -> {
                    final AudioChannel channel = channelInstance instanceof VoiceChannel ?
                            member.getGuild().getVoiceChannelById(channelInstance.getId()) :
                            null; // they doesn't support stage channels
                    if (channel == null) {
                        DiSky.exception(new DiSkyException("You can only move member to a voice channel, not a stage channel."), getNode());
                        restart();
                    }
                    member.getGuild().moveVoiceMember(member, (VoiceChannel) channel).queue(this::restart);
                });
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "move " + exprMember.toString(e, debug) + " to channel " + exprChannel.toString(e, debug) + " using bot " +exprBot.toString(e, debug);
    }

    @Override
    public boolean initEffect(Expression @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprMember = (Expression<Member>) exprs[0];
        exprChannel = (Expression<AudioChannel>) exprs[1];

        exprBot = (Expression<Bot>) exprs[2];
        if (exprBot == null)
            exprBot = Utils.defaultToEventValue(exprBot, Bot.class);
        if (exprBot == null)
        {
            Skript.error("Unable to get the bot in a move member effect.");
            return false;
        }

        return true;
    }
}
