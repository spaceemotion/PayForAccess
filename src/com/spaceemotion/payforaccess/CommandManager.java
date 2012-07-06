package com.spaceemotion.payforaccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.spaceemotion.payforaccess.command.AbstractCommand;
import com.spaceemotion.payforaccess.command.AddCommand;
import com.spaceemotion.payforaccess.command.CreateCommand;
import com.spaceemotion.payforaccess.command.EditCommand;
import com.spaceemotion.payforaccess.command.HelpCommand;
import com.spaceemotion.payforaccess.command.ListCommand;
import com.spaceemotion.payforaccess.command.RemoveCommand;
import com.spaceemotion.payforaccess.command.SelectCommand;
import com.spaceemotion.payforaccess.util.ChatUtil;
import com.spaceemotion.payforaccess.util.LanguageUtil;
import com.spaceemotion.payforaccess.util.MessageUtil;


public class CommandManager implements CommandExecutor {
	private static PayForAccessPlugin plugin;
	private static CommandSender sender;

	private static Map<String, AbstractCommand> cmdMap;
	private static List<String> indexList;


	public CommandManager(PayForAccessPlugin plugin) {
		CommandManager.plugin = plugin;
		cmdMap = new HashMap<String, AbstractCommand>();
		indexList = new ArrayList<String>();

		registerCommands();
	}

	private void registerCommands() {
		addCommand("help", new HelpCommand(plugin));
		addCommand("create", new CreateCommand(plugin));
		addCommand("add", new AddCommand(plugin));
		addCommand("remove", new RemoveCommand(plugin));
		addCommand("select", new SelectCommand(plugin));
		addCommand("edit", new EditCommand(plugin));
		addCommand("list", new ListCommand(plugin));
	}

	private void addCommand(String name, AbstractCommand cmd) {
		cmdMap.put(name.toLowerCase(), cmd);
		indexList.add(name);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager.sender = sender;

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player!");
			return false;
		}

		Player player = (Player) sender;
		AbstractCommand cmd = null;

		if (args.length > 0 && indexList.contains(args[0].toLowerCase())) {
			cmd = (AbstractCommand) cmdMap.get(args[0].toLowerCase());
		} else if (args.length > 0) {
			ChatUtil.sendPlayerMessage(player, ChatColor.RED + "Error: " + MessageUtil.parseMessage("error.commandnotfound", args[0]));
		} else {
			cmd = cmdMap.get("help");
		}

		if (cmd != null) {
			if (player.hasPermission(cmd.getPermission())) {
				if (!cmd.execute(player, args)) {
					ChatUtil.sendPlayerMessage(player, ChatColor.RED + "Error: " + cmd.getLastError());
				}

				cmd.clearErrors();
			} else ChatUtil.sendPlayerMessage(player, LanguageUtil.getString("error.nopermission"));

			return true;
		}

		return false;
	}

	/**
	 * Get the list of available commands
	 * 
	 * @return The command list
	 */
	public static Map<String, AbstractCommand> getCommandList() {
		return cmdMap;
	}

	/**
	 * Get the index list of available commands
	 * 
	 * @return The index list
	 */
	public static List<String> getIndexList() {
		return indexList;
	}

	/**
	 * Get the plugin instance
	 * 
	 * @return PayToEnterPlugin instance
	 */
	public static PayForAccessPlugin getPlugin() {
		return plugin;
	}

	/**
	 * Get the latest command sender
	 * 
	 * @return CommandSender the latest command sender
	 */
	public static CommandSender getSender() {
		return sender;
	}
}
