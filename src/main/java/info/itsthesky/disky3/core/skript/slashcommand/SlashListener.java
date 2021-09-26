package info.itsthesky.disky3.core.skript.slashcommand;

import info.itsthesky.disky3.core.skript.slashcommand.api.SlashObject;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SlashListener extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        for (SlashObject cmd : SlashFactory.getInstance().commandMap.values()) {
            if (!cmd.getName().equalsIgnoreCase(event.getName()))
                continue;
            boolean isGuild = event.isFromGuild();
            cmd.execute(new SlashEvent(
                    event,
                    cmd,
                    event.getOptions(),
                    isGuild ? event.getGuild() : null,
                    event.getChannel(),
                    isGuild ? event.getGuildChannel() : null,
                    event.getUser(),
                    isGuild ? event.getMember() : null,
                    event.getJDA()
            ));
        }
    }

}
