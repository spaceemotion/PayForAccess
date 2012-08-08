package com.spaceemotion.payforaccess.util;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.spaceemotion.payforaccess.CommandManager;


public class ChatUtil {
	public static void sendMessage(String message) {
		sendMessage(message, true);
	}

	public static String sendMessage(String message, boolean send) {
		return sendMessage(message, send, true);
	}

	public static String sendMessage(String message, boolean send, boolean prefix) {
		if (message != null) {
			if (prefix) message = getPrefix() + message;
			message = MessageUtil.parseColors(message);

			if (send) {
				CommandSender sender = CommandManager.getSender();
				sender.sendMessage(message);
			}

			return message;
		}

		return null;
	}

	public static void sendPlayerMessage(Player player, String message) {
		if (message != null) player.sendMessage(sendMessage(message, false));
	}

	public static void splitSendMessage(String[] messages) {
		splitSendMessage(messages, true);
	}

	public static void splitSendMessage(String[] messages, boolean prefix) {
		for (String msg : messages) {
			sendMessage(msg, true, prefix);
		}
	}

	public static void log(String msg) {
		CommandManager.getPlugin().getLogger().info(msg);
	}

	private static String getPrefix() {
		return MessageUtil.parseColors(LanguageUtil.getString("prefix")) + ChatColor.RESET;
	}
}
