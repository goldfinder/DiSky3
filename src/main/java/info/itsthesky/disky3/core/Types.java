
package info.itsthesky.disky3.core;

import ch.njol.skript.classes.Comparator;
import ch.njol.skript.registrations.Comparators;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.DiSkyException;
import info.itsthesky.disky3.api.DiSkyType;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.bot.Bot;
import info.itsthesky.disky3.api.bot.BotManager;
import info.itsthesky.disky3.api.emojis.Emote;
import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.music.AudioSite;
import info.itsthesky.disky3.api.skript.DiSkyComparator;
import info.itsthesky.disky3.api.wrapper.ButtonRow;
import info.itsthesky.disky3.core.commands.CommandObject;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.AttachmentOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

import static info.itsthesky.disky3.api.StaticData.LAST_GUILD_COMMAND;

public class Types {

	public static class DiSkyComparators {

		static {
			/*
			 * Basics ISnowflakes entities which can be handled by the disky comparator
			 */
			Comparators.registerComparator(Member.class, Member.class, new DiSkyComparator<>());
			Comparators.registerComparator(User.class, User.class, new DiSkyComparator<>());
			Comparators.registerComparator(Emote.class, Emote.class, new DiSkyComparator<>());
			Comparators.registerComparator(GuildChannel.class, GuildChannel.class, new DiSkyComparator<>());
			Comparators.registerComparator(MessageSticker.class, MessageSticker.class, new DiSkyComparator<>());
			Comparators.registerComparator(TextChannel.class, TextChannel.class, new DiSkyComparator<>());
			Comparators.registerComparator(VoiceChannel.class, VoiceChannel.class, new DiSkyComparator<>());
			Comparators.registerComparator(Guild.class, Guild.class, new DiSkyComparator<>());
			Comparators.registerComparator(UpdatingMessage.class, UpdatingMessage.class, new DiSkyComparator<>());
			Comparators.registerComparator(Category.class, Category.class, new DiSkyComparator<>());
			Comparators.registerComparator(Role.class, Role.class, new DiSkyComparator<>());
			Comparators.registerComparator(Webhook.class, Webhook.class, new DiSkyComparator<>());

			/*
			 * Custom entities which need a precise comparator
			 */
			Comparators.registerComparator(Bot.class, Bot.class, new Comparator<Bot, Bot>() {
				@Override
				public @NotNull Relation compare(@NotNull Bot jda, @NotNull Bot jda2) {
					if (jda.getCore().getSelfUser().getId().equals(jda2.getCore().getSelfUser().getId()))
						return Relation.EQUAL;
					return Relation.NOT_EQUAL;
				}

				@Override
				public boolean supportsOrdering() {
					return false;
				}
			});
		}

	}

	static {

		new DiSkyType<>(Category.class, "category", "categor(y|ies)", Category::getName, input -> BotManager.globalSearch(bot -> bot.getCore().getCategoryById(input)), false).register();
		new DiSkyType<>(VoiceChannel.class, "voicechannel", "voicechannels?", VoiceChannel::getName, input -> BotManager.globalSearch(bot -> bot.getCore().getVoiceChannelById(input)), false).register();
		new DiSkyType<>(TextChannel.class, "textchannel", "textchannels?", TextChannel::getName, input -> BotManager.globalSearch(bot -> bot.getCore().getTextChannelById(input)), false).register();
		new DiSkyType<>(NewsChannel.class, "newschannel", "newschannels?", NewsChannel::getName, input -> BotManager.globalSearch(bot -> bot.getCore().getNewsChannelById(input)), false).register();
		new DiSkyType<>(GuildChannel.class, "channel", "channels?", GuildChannel::getName, input -> BotManager.globalSearch(bot -> bot.getCore().getGuildChannelById(input)), false).register();
		new DiSkyType<>(GuildThread.class, "thread", "threads?", Channel::getName, input -> BotManager.globalSearch(bot -> bot.getCore().getGuildThreadById(input)), false).register();

		new DiSkyType<>(MessageSticker.class, "sticker", "stickers?", MessageSticker::getName, null, false).register();
		new DiSkyType<>(User.class, "user", "users?", User::getName, input -> BotManager.globalSearch(bot -> bot.getCore().getUserById(input)), false).register();
		new DiSkyType<>(Role.class, "role", "roles?", Role::getName, input -> BotManager.globalSearch(bot -> bot.getCore().getRoleById(input)), false).register();
		new DiSkyType<>(ButtonRow.class, "buttonrow", "buttonrows?", ButtonRow::toString, null, false).register();
		new DiSkyType<>(Button.class, "button", "buttons?").register();
		new DiSkyType<>(SelectionMenu.Builder.class, "selectbuilder", "selectbuilders?").register();
		new DiSkyType<>(SelectOption.class, "selectchoice", "selectchoices?").register();
		new DiSkyType<>(Guild.class, "guild", "guilds?", Guild::getName, input -> BotManager.globalSearch(bot -> bot.getCore().getGuildById(input)), false).register();
		new DiSkyType<>(Activity.class, "presence", "presences?", a -> a.getType().name().toLowerCase(Locale.ROOT).replaceAll("_", " ") + ": " + a.getName(), input -> null, false).register();
		new DiSkyType<>(Bot.class, "bot", "bots?", Bot::getName, null, false).register();
		new DiSkyType<>(Emote.class, "emote", "emotes?", Emote::getName, null, false).register();
		new DiSkyType<>(WebhookMessageBuilder.class, "webhookmessagebuilder", "webhookmessagebuilders?", null).register();
		new DiSkyType<>(UpdatingMessage.class, "message", "messages?", msg -> msg.getMessage().getContentRaw(), null, false).register();
		new DiSkyType<>(Webhook.class, "webhookbuilder", "webhookbuilders?", null).register();
		new DiSkyType<>(EmbedBuilder.class, "embedbuilder", "embedbuilders?", embed -> embed.getDescriptionBuilder().toString(), null, false).register();
		new DiSkyType<>(CommandObject.class, "discordcommand", "discordcommands?", CommandObject::getName, null, false).register();
		new DiSkyType<>(Invite.class, "invite", "invites?", Invite::getUrl, null, false).register();
		new DiSkyType<>(MessageBuilder.class, "messagebuilder", "messagebuilders?", msg -> msg.getStringBuilder().toString(), null, false).register();
		new DiSkyType<>(Message.Attachment.class, "attachment", "attachments?", Message.Attachment::getFileName, null, false).register();
		new DiSkyType<>(AudioTrack.class, "track", "tracks?", track -> track.getInfo().title, null, false).register();

		DiSkyType.fromEnum(Permission.class, "permission", "permissions?").register();
		DiSkyType.fromEnum(AttachmentOption.class, "attachmentoption", "attachmentoptions?").register();
		DiSkyType.fromEnum(ButtonStyle.class, "buttonstyle", "buttonstyles?").register();
		DiSkyType.fromEnum(GatewayIntent.class, "intent", "intents?").register();
		DiSkyType.fromEnum(OnlineStatus.class, "onlinestatus", "onlinestatus").register();
		DiSkyType.fromEnum(OptionType.class, "optiontype", "optiontypes?").register();
		DiSkyType.fromEnum(Region.class, "channelregion", "channelregions?").register();
		DiSkyType.fromEnum(AudioSite.class, "audiosite", "audiosites?").register();

		new DiSkyType<>(Member.class, "member", "members?", Member::getEffectiveName, input -> {
			final @Nullable Guild guild = LAST_GUILD_COMMAND;
			if (guild == null) {
				DiSky.exception(new DiSkyException("DiSky tried to parse a member argument, however the event-guild cannot be found."), null);
				return null;
			} else {
				return guild.getMemberById(Utils.parseLong(input, false, true));
			}
		}, false).register();
	}
}