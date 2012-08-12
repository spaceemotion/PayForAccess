package com.spaceemotion.payforaccess.util;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.spaceemotion.payforaccess.CommandManager;


public class CommandUtil {
	public static void execAsServer(String cmd) {
		CommandManager.getPlugin().getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
	}

	public static void execAsPlayer(String cmd, String player) {
		Server server = CommandManager.getPlugin().getServer();

		if (server.getPlayer(player) != null) execAsPlayer(cmd, player);
	}

	public static void execAsPlayer(String cmd, Player player) {
		player.performCommand(cmd);
	}
}
