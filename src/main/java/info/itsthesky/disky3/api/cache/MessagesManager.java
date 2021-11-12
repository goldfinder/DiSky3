package info.itsthesky.disky3.api.cache;

import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.core.events.message.MessageDelete;
import info.itsthesky.disky3.core.events.message.MessageEdit;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.exceptions.MissingAccessException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by ItsTheSky the 25/03/2021 <br>
 * Allow the JDA to cache message with simple wrapper.
 */
public class MessagesManager extends ListenerAdapter {

    private static final HashMap<String, CachedMessage> previousMessages = new HashMap<>();
    private static final HashMap<String, CachedMessage> newMessages = new HashMap<>();

    public static void editMessage(Message message) {
        previousMessages.put(message.getId(), new CachedMessage(message));
    }

    public static CachedMessage retrieveNewMessage(String id) {
        AtomicReference<CachedMessage> finalMessage = new AtomicReference<>();
        newMessages.forEach((msgID, cm) -> {
            if (cm.getMessageID().equalsIgnoreCase(id)) {
                finalMessage.set(cm);
            }
        });
        return finalMessage.get();
    }

    public static CachedMessage retrieveOldMessage(String id) {
        AtomicReference<CachedMessage> finalMessage = new AtomicReference<>();
        previousMessages.forEach((msgID, cm) -> {
            if (cm.getMessageID().equalsIgnoreCase(id)) {
                finalMessage.set(cm);
            }
        });
        return finalMessage.get();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        previousMessages.put(e.getMessageId(), new CachedMessage(e.getMessage()));
    }


    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        if (e.getUser().isBot() || e.getUser().getId().equalsIgnoreCase(e.getJDA().getSelfUser().getId())) {
            for (TextChannel channel : e.getGuild().getTextChannels()) {
                for (Message message : channel.getHistory().getRetrievedHistory()) {
                    previousMessages.put(message.getId(), new CachedMessage(message));
                }
            }
        }
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent e) {
        MessageDelete.content = previousMessages.get(e.getMessageId()).getContent();
        MessageDelete.id = e.getMessageIdLong();
    }

    @Override
    public void onReady(ReadyEvent e) {
        for (Guild guild : e.getJDA().getGuilds()) {
            Utils.async(() -> {
                long start = System.currentTimeMillis();
                for (TextChannel channel : guild.getTextChannels()) {
                    try {
                        channel.getIterableHistory().queue(history -> {
                            if (history == null) return;
                            for (Message message : history) {
                                previousMessages.put(message.getId(), new CachedMessage(message));
                            }
                        });
                    } catch (MissingAccessException ex) {
                        DiSky.getInstance().getLogger().warning("DiSky cannot cache message for the message edit event since the bot doesn't have the " + ex.getPermission().getName() + " permission in "+ guild.getName() +"!");
                    }
                }
            });
        }
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent e) {
        if (!previousMessages.containsKey(e.getMessageId())) {
            previousMessages.put(e.getMessageId(), new CachedMessage(e.getMessage()));
        }
        newMessages.put(e.getMessageId(), new CachedMessage(e.getMessage()));
        MessageEdit.oldContent = retrieveOldMessage(e.getMessageId()).getContent();
        MessageEdit.newContent = retrieveNewMessage(e.getMessageId()).getContent();
    }

}